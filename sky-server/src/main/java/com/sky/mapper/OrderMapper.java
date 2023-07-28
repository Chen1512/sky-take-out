package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

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
}
