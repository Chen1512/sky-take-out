package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shkstart
 * @create 2023--28-20:03
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;

    /**
     * @Description:用户下单
     * @return: com.sky.result.Result<com.sky.vo.OrderSubmitVO>
     * @author: chen
     * @date: 2023/7/28 20:07
     */
    @Override
    @Transactional
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        //异常情况的处理（收货地址为空、超出配送范围、购物车为空）
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook==null){
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        ShoppingCart shoppingCart = ShoppingCart.builder().userId(BaseContext.getCurrentId()).build();
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        if (list==null||list.size()==0){
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        //构造订单数据
        Orders order = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO,order);
        order.setPhone(addressBook.getPhone());
        order.setAddress(addressBook.getDetail());
        order.setConsignee(addressBook.getConsignee());
        order.setNumber(String.valueOf(System.currentTimeMillis()));
        order.setUserId(BaseContext.getCurrentId());
        order.setStatus(Orders.PENDING_PAYMENT);
        order.setPayStatus(Orders.UN_PAID);
        order.setOrderTime(LocalDateTime.now());
        //向订单表插入1条数据
        orderMapper.insert(order);

        //订单明细数据
        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
        for (ShoppingCart cart : list) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart,orderDetail);
            orderDetail.setOrderId(order.getId());
            orderDetails.add(orderDetail);
        }
        //向明细表插入n条数据
        orderDetailMapper.insertBatch(orderDetails);

        //清理购物车中的数据
        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());

        //封装返回结果
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder().id(order.getId()).orderAmount(order.getAmount()).orderNumber(order.getNumber()).orderTime(order.getOrderTime()).build();

        return  orderSubmitVO;

    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        //调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal(0.01), //支付金额，单位 元
                "苍穹外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();

        // 根据订单号查询当前用户的订单
        Orders ordersDB = orderMapper.getByNumberAndUserId(outTradeNo, userId);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }

    /**
     * @Description:历史订单查询
     * @return:
     * @author: chen
     * @date: 2023/7/29 16:33
     */
    @Override
    public PageResult historyOrders(int pageNum, int pageSize, Integer status) {
        PageHelper.startPage(pageNum,pageSize);
        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        ordersPageQueryDTO.setStatus(status);
        ArrayList<OrderVO> list = new ArrayList<>();
        List<Orders> orderList=orderMapper.list(ordersPageQueryDTO);
        if (orderList!=null&&orderList.size()>0){
            orderList.forEach(order -> {
                OrderVO orderVO = new OrderVO();
                Long orderId = order.getId();
                List<OrderDetail> orderDetailList=orderDetailMapper.getByOrderId(orderId);
                BeanUtils.copyProperties(order,orderVO);
                orderVO.setOrderDetailList(orderDetailList);
                list.add(orderVO);
            });
        }

        PageResult pageResult = new PageResult(list.size(),list);
        return pageResult;
    }

    /**
     * @Description:查询订单详情
     * @return: com.sky.vo.OrderVO
     * @author: chen
     * @date: 2023/7/29 17:31
     */
    @Override
    public OrderVO orderDetail(Long id) {
        Orders order= orderMapper.getById(id);
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order,orderVO);
        orderVO.setOrderDetailList(orderDetailList);
        return orderVO;
    }

    /**
     * @Description:取消订单
     * @return: void
     * @author: chen
     * @date: 2023/7/30 11:34
     */
    @Override
    public void userCancelById(Long id) throws Exception {
        Orders order = orderMapper.getById(id);
        if (order==null){
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        if (order.getStatus()>2){
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        Orders orders = new Orders();
        orders.setId(id);
        // 订单处于待接单状态下取消，需要进行退款
        if (Orders.TO_BE_CONFIRMED.equals(order.getStatus())){
            //调用微信支付退款接口
            weChatPayUtil.refund(
                    order.getNumber(), //商户订单号
                    order.getNumber(), //商户退款单号
                    new BigDecimal(0.01),//退款金额，单位 元
                    new BigDecimal(0.01));//原订单金额

            //支付状态修改为 退款
            orders.setPayStatus(Orders.REFUND);
        }
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason("用户取消");
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    /**
     * @Description:再来一单
     * @return: void
     * @author: chen
     * @date: 2023/7/30 14:02
     */
    @Override
    public void repetition(Long id) {
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(id);
        Long userId = BaseContext.getCurrentId();
        ArrayList<ShoppingCart> shoppingCarts = new ArrayList<>();
        orderDetailList.forEach(orderDetail -> {
            ShoppingCart cart = new ShoppingCart();
            BeanUtils.copyProperties(orderDetail,cart);
            cart.setUserId(userId);
            cart.setCreateTime(LocalDateTime.now());
            shoppingCarts.add(cart);
        });
        shoppingCartMapper.insertBatch(shoppingCarts);
    }
}
