package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

/**
 * @author shkstart
 * @create 2023--28-19:59
 */
public interface OrderService {
    /**
     * @Description:用户下单
     * @return: com.sky.result.Result<com.sky.vo.OrderSubmitVO>
     * @author: chen
     * @date: 2023/7/28 20:07
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);
    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * @Description:历史订单查询
     * @return:
     * @author: chen
     * @date: 2023/7/29 16:33
     */
    PageResult historyOrders(int pageNum, int pageSize, Integer status);

    /**
     * @Description:查询订单详情
     * @return: com.sky.vo.OrderVO
     * @author: chen
     * @date: 2023/7/29 17:31
     */
    OrderVO detail(Long id);

    /**
     * @Description:取消订单
     * @return: void
     * @author: chen
     * @date: 2023/7/30 11:34
     */
    void userCancelById(Long id) throws Exception;

    /**
     * @Description:再来一单
     * @return: void
     * @author: chen
     * @date: 2023/7/30 14:01
     */
    void repetition(Long id);

    /**
     * @Description:订单搜索
     * @return: com.sky.result.PageResult
     * @author: chen
     * @date: 2023/7/30 14:40
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * @Description:各个状态的订单数量统计
     * @return: com.sky.vo.OrderStatisticsVO
     * @author: chen
     * @date: 2023/7/30 23:38
     */
    OrderStatisticsVO statistics();

    /**
     * @Description:接单
     * @return: void
     * @author: chen
     * @date: 2023/7/31 9:21
     * @param ordersConfirmDTO
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * @Description:拒单
     * @return: void
     * @author: chen
     * @date: 2023/7/31 9:34
     */
    void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception;

    /**
     * @Description:派送订单
     * @return: void
     * @author: chen
     * @date: 2023/7/31 10:25
     */
    void delivery(Long id);

    /**
     * @Description:完成订单
     * @return: void
     * @author: chen
     * @date: 2023/7/31 10:33
     */
    void complete(Long id);
}
