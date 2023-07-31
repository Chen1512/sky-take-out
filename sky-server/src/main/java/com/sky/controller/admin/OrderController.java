package com.sky.controller.admin;


import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

/**订单管理
 * @author shkstart
 * @create 2023--28-19:58
 */
@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Api(tags = "订单管理接口")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * @Description:订单搜索
     * @return: com.sky.result.Result<com.sky.result.PageResult>
     * @author: chen
     * @date: 2023/7/30 14:39
     */
    @GetMapping("/conditionSearch")
    @ApiOperation("订单搜索")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO){
       PageResult pageResult=orderService.conditionSearch(ordersPageQueryDTO);
       return Result.success(pageResult);
    }

    /**
     * @Description:查询订单详情
     * @return: com.sky.result.Result<com.sky.vo.OrderVO>
     * @author: chen
     * @date: 2023/7/30 23:27
     */
    @GetMapping("/details/{id}")
    @ApiOperation("查询订单详情")
    public Result<OrderVO> adminDetails(@PathVariable Long id){
        OrderVO orderVO= orderService.detail(id);
        return Result.success(orderVO);
    }

    /**
     * @Description:各个状态的订单数量统计
     * @return: com.sky.result.Result<com.sky.vo.OrderStatisticsVO>
     * @author: chen
     * @date: 2023/7/30 23:37
     */
    @GetMapping("/statistics")
    @ApiOperation("各个状态的订单数量统计")
    public Result<OrderStatisticsVO> statistics(){
        OrderStatisticsVO orderStatisticsVO=orderService.statistics();
        return Result.success(orderStatisticsVO);
    }

    /**
     * @Description:接单
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/31 9:16
     */
    @PutMapping("/confirm")
    @ApiOperation("接单")
    public Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO){
        orderService.confirm(ordersConfirmDTO);
        return Result.success();
    }

    /**
     * @Description:拒单
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/31 9:33
     */
    @PutMapping("/rejection")
    @ApiOperation("拒单")
    public Result rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        orderService.rejection(ordersRejectionDTO);
        return Result.success();
    }

    /**
     * @Description:派送订单
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/31 10:24
     */
    @PutMapping("/delivery/{id}")
    @ApiOperation("派送订单")
    public Result delivery(@PathVariable Long id){
        orderService.delivery(id);
        return Result.success();
    }

    /**
     * @Description:完成订单
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/31 10:32
     */
    @PutMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public Result complete(@PathVariable Long id){
        orderService.complete(id);
        return Result.success();
    }
}
