package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * @Description:添加菜品
     * @return: void
     * @author: chen
     * @date: 2023/7/12 16:29
     */
    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    /**
     * @Description:菜品分页查询
     * @return: com.github.pagehelper.Page<com.sky.vo.DishVO>
     * @author: chen
     * @date: 2023/7/12 18:54
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * @Description:根据id查询菜品
     * @return: com.sky.entity.Dish
     * @author: chen
     * @date: 2023/7/12 20:54
     */
    @Select("select * from dish where id=#{id}")
    Dish getByid(Long id);

    /**
     * @Description:根据id删除菜品信息
     * @return: void
     * @author: chen
     * @date: 2023/7/12 21:12
     */
    @Delete("delete from dish where id=#{id}")
    void deleteById(Long id);

    /**
     * @Description:修改菜品
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/12 21:52
     */
    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    /**
     * @Description:根据分类id和状态，查询菜品
     * @return: java.util.List<com.sky.entity.Dish>
     * @author: chen
     * @date: 2023/7/14 15:45
     */
    @Select("select * from dish where category_id=#{categoryId} and status=#{status}")
    List<Dish> list(Dish dish);

    /**
     * @Description:根据套餐id查询菜品
     * @return: java.util.List<com.sky.entity.Dish>
     * @author: chen
     * @date: 2023/7/14 15:43
     */
    @Select("select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = #{setmealId} ")
    List<Dish> getBySetmealId(Long setmealId);
}
