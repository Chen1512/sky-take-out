package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shkstart
 * @create 2023--28-20:05
 */
@Mapper
public interface OrderDetailMapper {
    /**
     * @Description:批量插入订单明细数据
     * @return: void
     * @author: chen
     * @date: 2023/7/28 20:48
     */
    void insertBatch(ArrayList<OrderDetail> orderDetails);

    @Select("select * from order_detail where order_id=#{orderId}")
    List<OrderDetail> getByOrderId(Long orderId);
}
