package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 套餐业务实现
 */
@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }

    /**
     * @Description:添加套餐
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/14 13:11
     */
    @Override
    @Transactional
    public void save(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.insert(setmeal);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmeal.getId());
        });
        setmealDishMapper.insertBatch(setmealDishes);
    }

    /**
     * @Description:分页查询
     * @return: com.sky.result.Result<com.sky.result.PageResult>
     * @author: chen
     * @date: 2023/7/14 14:31
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page=setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * @Description:批量删除套餐
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/14 14:53
     */
    @Override
    public void deleteBatch(List<Long> ids) {
        ids.forEach(id->{
            Setmeal setmeal=setmealMapper.getByid(id);
            if (StatusConstant.ENABLE==setmeal.getStatus()){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });

        ids.forEach(setmealId->{
            setmealMapper.deleteById(setmealId);
            setmealDishMapper.deleteBySetmealId(setmealId);
        });
    }

    /**
     * @Description:根据id查询套餐
     * @return: com.sky.vo.SetmealVO
     * @author: chen
     * @date: 2023/7/14 15:09
     * @param id
     */
    @Override
    public SetmealVO getById(Long id) {
        Setmeal setmeal = setmealMapper.getByid(id);
        List<SetmealDish> list=setmealDishMapper.getBySetmealId(id);
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setSetmealDishes(list);
        return setmealVO;
    }

    /**
     * @Description:修改套餐
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/14 15:21
     */
    @Override
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.update(setmeal);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if (setmealDishes!=null&&setmealDishes.size()>0){
            setmealDishMapper.deleteBySetmealId(setmealDTO.getId());
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealDTO.getId());
            });
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }

    /**
     * @Description:套餐起售、停售
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/14 15:35
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        if (StatusConstant.ENABLE==status){
            List<Dish> dishList = dishMapper.getBySetmealId(id);
            if (dishList!=null&&dishList.size()>0){
                dishList.forEach(dish -> {
                    if (StatusConstant.DISABLE==dish.getStatus()){
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }
        Setmeal setmeal = Setmeal.builder().id(id).status(status).build();
        setmealMapper.update(setmeal);
    }


}
