package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shkstart
 * @create 2023--01-16:41
 */
@RestController
@RequestMapping("/admin/workspace")
@Slf4j
public class WorkspaceController {
    @Autowired
    private WorkspaceService workspaceService;

    /**
     * @Description:查询今日运营数据
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/8/1 16:44
     */
    @ApiOperation("查询今日运营数据")
    @GetMapping("/businessData")
    public Result<BusinessDataVO> businessData(){
        BusinessDataVO businessDataVO = workspaceService.businessData();
        return Result.success(businessDataVO);
    }

    /**
     * @Description:查询订单管理数据
     * @return: com.sky.result.Result<com.sky.vo.OrderOverViewVO>
     * @author: chen
     * @date: 2023/8/1 17:12
     */
    @ApiOperation("查询订单管理数据")
    @GetMapping("/overviewOrders")
    public Result<OrderOverViewVO> overviewOrders(){
        OrderOverViewVO orderOverViewVO = workspaceService.overviewOrders();
        return Result.success(orderOverViewVO);
    }

    /**
     * @Description:查询套餐总览
     * @return: com.sky.result.Result<com.sky.vo.SetmealOverViewVO>
     * @author: chen
     * @date: 2023/8/1 17:21
     */
    @ApiOperation("查询套餐总览")
    @GetMapping("/overviewSetmeals")
    public Result<SetmealOverViewVO> overviewSetmeals(){
        SetmealOverViewVO setmealOverViewVO = workspaceService.overviewSetmeals();
        return Result.success(setmealOverViewVO);
    }

    /**
     * @Description:查询菜品总览
     * @return: com.sky.result.Result<com.sky.vo.DishOverViewVO>
     * @author: chen
     * @date: 2023/8/1 17:30
     */
    @ApiOperation("查询菜品总览")
    @GetMapping("/overviewDishes")
    public Result<DishOverViewVO> overviewDishes(){
        DishOverViewVO dishOverViewVO = workspaceService.overviewDishes();
        return Result.success(dishOverViewVO);
    }
}
