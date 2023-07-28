package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import java.util.List;

public interface SetmealService {

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);

    /**
     * @Description:添加套餐
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/14 13:11
     */
    void save(SetmealDTO setmealDTO);

    /**
     * @Description:分页查询
     * @return: com.sky.result.Result<com.sky.result.PageResult>
     * @author: chen
     * @date: 2023/7/14 14:31
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * @Description:批量删除套餐
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/14 14:53
     */
    void deleteBatch(List<Long> ids);

    /**
     * @Description:根据id查询套餐
     * @return: com.sky.vo.SetmealVO
     * @author: chen
     * @date: 2023/7/14 15:09
     * @param id
     */
    SetmealVO getById(Long id);

    /**
     * @Description:修改套餐
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/14 15:21
     */
    void update(SetmealDTO setmealDTO);

    /**
     * @Description:套餐起售、停售
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/14 15:35
     */
    void startOrStop(Integer status, Long id);
}
