package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;

import java.time.LocalDateTime;

/**
 * @author shkstart
 * @create 2023--01-16:42
 */
public interface WorkspaceService {
    /**
     * @Description:查询今日运营数据
     * @return: com.sky.vo.BusinessDataVO
     * @author: chen
     * @date: 2023/8/1 16:45
     * @param begin
     * @param end
     */
    BusinessDataVO businessData(LocalDateTime begin, LocalDateTime end);

    /**
     * @Description:查询订单管理数据
     * @return: com.sky.vo.OrderOverViewVO
     * @author: chen
     * @date: 2023/8/1 17:13
     */
    OrderOverViewVO overviewOrders();

    /**
     * @Description:查询套餐总览
     * @return: com.sky.vo.SetmealOverViewVO
     * @author: chen
     * @date: 2023/8/1 17:22
     */
    SetmealOverViewVO overviewSetmeals();

    /**
     * @Description:查询菜品总览
     * @return: com.sky.vo.DishOverViewVO
     * @author: chen
     * @date: 2023/8/1 17:31
     */
    DishOverViewVO overviewDishes();
}
