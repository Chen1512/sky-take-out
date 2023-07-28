package com.sky.mapper;

import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    /**
     * @Description:批量添加套餐菜品信息
     * @return: void
     * @author: chen
     * @date: 2023/7/14 13:29
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * @Description:根据套餐id删除对应的数据
     * @return: void
     * @author: chen
     * @date: 2023/7/14 15:04
     */
    @Delete("delete from setmeal_dish where setmeal_id=#{setmealId}")
    void deleteBySetmealId(Long setmealId);

    /**
     * @Description:根据套餐id查询对应的数据
     * @return: java.util.List<com.sky.entity.SetmealDish>
     * @author: chen
     * @date: 2023/7/14 15:13
     */
    @Select("select * from setmeal_dish where setmeal_id=#{setmealId}")
    List<SetmealDish> getBySetmealId(Long setmealId);
}
