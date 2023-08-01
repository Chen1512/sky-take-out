package com.sky.mapper;

import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderStatisticsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author shkstart
 * @create 2023--28-20:04
 */
@Mapper
public interface OrderMapper {
    /**
     * @Description:插入订单数据
     * @return: void
     * @author: chen
     * @date: 2023/7/28 20:49
     */
    void insert(Orders order);
    /**
     * 根据订单号和用户id查询订单
     * @param orderNumber
     * @param userId
     */
    @Select("select * from orders where number = #{orderNumber} and user_id= #{userId}")
    Orders getByNumberAndUserId(String orderNumber, Long userId);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    /**
     * @Description:订单搜索
     * @return: java.util.List<com.sky.entity.Orders>
     * @author: chen
     * @date: 2023/7/30 23:52
     */
    List<Orders> list(OrdersPageQueryDTO ordersPageQueryDTO);


    /**
     * @Description:查询订单详情
     * @return: com.sky.entity.Orders
     * @author: chen
     * @date: 2023/7/30 23:58
     */
    @Select("select * from orders where id=#{id}")
    Orders getById(Long id);

    /**
     * @Description:各个状态的订单数量统计
     * @return: java.lang.Integer
     * @author: chen
     * @date: 2023/7/30 23:51
     */
    @Select("select count(*) from orders where status=#{status}")
    Integer countStatus(Integer status);

    /**
     * @Description:根据状态和下单时间查询订单
     * @return: java.util.List<com.sky.entity.Orders>
     * @author: chen
     * @date: 2023/7/31 16:07
     */
    @Select("select * from orders where status=#{status} and order_time<#{orderTime}")
    List<Orders> getByStatusAndOrdertimeLT(Integer status, LocalDateTime orderTime);

    /**
     * @Description:营业额统计接口
     * @return: java.lang.Double
     * @author: chen
     * @date: 2023/8/1 11:16
     */
    Double sumByMap(Map map);

    /**
     * @Description:根据动态条件统计订单数量
     * @return: java.lang.Integer
     * @author: chen
     * @date: 2023/8/1 14:37
     */
    Integer countByMap(Map map);

    /**
     * @Description:查询销量排名top10接口
     * @return: java.util.List<com.sky.dto.GoodsSalesDTO>
     * @author: chen
     * @date: 2023/8/1 15:48
     */
    List<GoodsSalesDTO> getSalesTop10(LocalDateTime begin, LocalDateTime end);
}
