package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealMapper {

    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * @Description:根据id修改套餐
     * @return: void
     * @author: chen
     * @date: 2023/7/12 23:35
     */
    void update(Setmeal setmeal);

    /**
     * 动态条件查询套餐
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐id查询菜品选项
     * @param setmealId
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);

    /**
     * @Description:添加套餐
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/14 13:11
     */
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * @Description:分页查询
     * @return: com.github.pagehelper.Page<com.sky.vo.SetmealVO>
     * @author: chen
     * @date: 2023/7/14 14:59
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * @Description:根据id查询套餐信息
     * @return: com.sky.entity.Setmeal
     * @author: chen
     * @date: 2023/7/14 14:59
     */
    @Select("select * from setmeal where id=#{id}")
    Setmeal getByid(Long id);

    /**
     * @Description:根据id删除套餐
     * @return: void
     * @author: chen
     * @date: 2023/7/14 15:03
     */
    @Delete("delete from setmeal where id=#{setmealId}")
    void deleteById(Long setmealId);
}
