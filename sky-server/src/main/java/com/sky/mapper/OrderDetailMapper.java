package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

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
}
