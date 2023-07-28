package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.DishVO;

import java.util.List;

/**
 * @author shkstart
 * @create 2023--12-16:00
 */
public interface DishService {
    /**
     * @Description:新增菜品
     * @return: void
     * @author: chen
     * @date: 2023/7/12 16:04
     */
    void saveWithFlavor(DishDTO dishDTO);

    /**
     * @Description:菜品分页查询
     * @return: com.sky.result.Result<com.sky.vo.DishVO>
     * @author: chen
     * @date: 2023/7/12 17:31
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**批量删除菜品
     * @Description:
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/12 18:53
     */
    void deleteBatch(List<Long> ids);

    /**
     * @Description:根据id查询菜品
     * @return: com.sky.result.Result<com.sky.vo.DishVO>
     * @author: chen
     * @date: 2023/7/12 21:42
     */
    DishVO getById(Long id);

    /**
     * @Description:修改菜品
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/12 21:52
     */
    void update(DishDTO dishDTO);

    /**
     * @Description:菜品的起售、停售
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/12 23:02
     */
    void startOrStop(Integer status, Long id);

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);

    /**
     * @Description:根据分类查询菜品
     * @return: java.util.List<com.sky.entity.Dish>
     * @author: chen
     * @date: 2023/7/14 13:46
     */
    List<Dish> list(Long categoryId);
}
