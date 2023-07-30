package com.sky.controller.user;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**订单
 * @author shkstart
 * @create 2023--28-19:58
 */
@RestController("userOrderController")
@RequestMapping("/user/order")
@Api(tags = "C端-订单接口")
public class OrderController {

    @Autowired
    private OrderService orderService;
    /**
     * @Description:用户下单
     * @return: com.sky.result.Result<com.sky.vo.OrderSubmitVO>
     * @author: chen
     * @date: 2023/7/28 20:07
     */
    @PostMapping("/submit")
    @ApiOperation("用户下单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        OrderSubmitVO orderSubmitVO =orderService.submitOrder(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }
    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        return Result.success(orderPaymentVO);
    }

    /**
     * @Description:历史订单查询
     * @return: com.sky.result.Result<com.sky.result.PageResult>
     * @author: chen
     * @date: 2023/7/29 17:25
     */
    @GetMapping("/historyOrders")
    @ApiOperation("历史订单查询")
    public Result<PageResult> historyOrders(int page,int pageSize,Integer status){
        PageResult pageResult=orderService.historyOrders(page,pageSize,status);
        return Result.success(pageResult);
    }

    /**
     * @Description:查询订单详情
     * @return: com.sky.result.Result<com.sky.vo.OrderVO>
     * @author: chen
     * @date: 2023/7/29 17:27
     */
    @GetMapping("/orderDetail/{id}")
    @ApiOperation("查询订单详情")
    public Result<OrderVO> orderDetail(@PathVariable Long id){
        OrderVO orderVO= orderService.orderDetail(id);
        return Result.success(orderVO);
    }

    /**
     * @Description:取消订单
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/30 11:34
     */
    @PutMapping("/cancel/{id}")
    @ApiOperation("取消订单")
    public Result cancel(@PathVariable Long id) throws Exception {
        orderService.userCancelById(id);
        return Result.success();
    }

    /**
     * @Description:再来一单
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/30 13:59
     */
    @PostMapping("/repetition/{id}")
    @ApiOperation("再来一单")
    public Result repetition(@PathVariable Long id){
        orderService.repetition(id);
        return Result.success();
    }

}
