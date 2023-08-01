package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkspaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author shkstart
 * @create 2023--01-16:42
 */
@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private DishMapper dishMapper;
    /**
     * @Description:查询今日运营数据
     * @return: com.sky.vo.BusinessDataVO
     * @author: chen
     * @date: 2023/8/1 16:45
     * @param begin
     * @param end
     */
    @Override
    public BusinessDataVO businessData(LocalDateTime begin, LocalDateTime end) {
        Map map = new HashMap();
        map.put("begin",begin);
        map.put("end",end);


        Integer newUsers = userMapper.countByMap(map)==null?0:userMapper.countByMap(map);
        Integer totle = orderMapper.countByMap(map)==null?0:orderMapper.countByMap(map);
        map.put("status", Orders.COMPLETED);
        Integer validOrderCount = orderMapper.countByMap(map)==null?0:orderMapper.countByMap(map);
        Double orderCompletionRate=0.0;
        if (totle!=null&&totle!=0){
            orderCompletionRate= Double.valueOf(validOrderCount/totle);
        }
        Double turnover = orderMapper.sumByMap(map)==null?0.0:orderMapper.sumByMap(map);
        Double unitPrice = 0.0;
        if (totle!=null&&totle!=0&&turnover!=null){
            unitPrice = turnover/totle;
        }
        BusinessDataVO businessDataVO = BusinessDataVO.builder().newUsers(newUsers).orderCompletionRate(orderCompletionRate).turnover(turnover).unitPrice(unitPrice).validOrderCount(validOrderCount).build();
        return businessDataVO;
    }

    /**
     * @Description:查询订单管理数据
     * @return: com.sky.vo.OrderOverViewVO
     * @author: chen
     * @date: 2023/8/1 17:13
     */
    @Override
    public OrderOverViewVO overviewOrders() {
        Integer allOrders = orderMapper.countByMap(new HashMap());
        Integer cancelledOrders = orderMapper.countStatus(Orders.CANCELLED);
        Integer completedOrders=orderMapper.countStatus(Orders.COMPLETED);
        Integer deliveredOrders=orderMapper.countStatus(Orders.CONFIRMED);
        Integer waitingOrders=orderMapper.countStatus(Orders.TO_BE_CONFIRMED);
        OrderOverViewVO orderOverViewVO = OrderOverViewVO.builder().cancelledOrders(cancelledOrders).completedOrders(completedOrders).deliveredOrders(deliveredOrders).waitingOrders(waitingOrders).allOrders(allOrders).build();
        return orderOverViewVO;
    }

    /**
     * @Description:查询套餐总览
     * @return: com.sky.vo.SetmealOverViewVO
     * @author: chen
     * @date: 2023/8/1 17:23
     */
    @Override
    public SetmealOverViewVO overviewSetmeals() {
        Integer status=0;
        Integer discontinued = setmealMapper.getByStatus(status);
        status=1;
        Integer sold = setmealMapper.getByStatus(status);
        SetmealOverViewVO setmealOverViewVO = SetmealOverViewVO.builder().discontinued(discontinued).sold(sold).build();
        return setmealOverViewVO;
    }

    /**
     * @Description:查询菜品总览
     * @return: com.sky.vo.DishOverViewVO
     * @author: chen
     * @date: 2023/8/1 17:31
     */
    @Override
    public DishOverViewVO overviewDishes() {
        Integer status=0;
        Integer discontinued = dishMapper.getByStatus(status);
        status=1;
        Integer sold = dishMapper.getByStatus(status);
        DishOverViewVO dishOverViewVO = DishOverViewVO.builder().discontinued(discontinued).sold(sold).build();
        return dishOverViewVO;
    }
}
