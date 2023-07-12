package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * @Description:根据菜品id查询对应的套餐id
     * @return: java.util.List<java.lang.Long>
     * @author: chen
     * @date: 2023/7/12 21:19
     */
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);

}
