package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.events.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author shkstart
 * @create 2023--12-16:01
 */
@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * @Description:新增菜品
     * @return: void
     * @author: chen
     * @date: 2023/7/12 16:04
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dish.setStatus(StatusConstant.ENABLE);
        dishMapper.insert(dish);
        Long id = dish.getId();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors!=null&&flavors.size()!=0){
            flavors.forEach(flavor->{
                flavor.setDishId(id);
            });
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * @Description:菜品分页查询
     * @return: com.sky.result.Result<com.sky.vo.DishVO>
     * @author: chen
     * @date: 2023/7/12 17:31
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page=dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**批量删除菜品
     * @Description:
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/12 18:53
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
        ids.forEach(id->{
            Dish dish=dishMapper.getByid(id);
            if (StatusConstant.ENABLE==dish.getStatus()){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        });

        List<Long> setmealIds=setmealDishMapper.getSetmealIdsByDishIds(ids);
        if (setmealIds!=null&&setmealIds.size()>0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        ids.forEach(id->{
            dishMapper.deleteById(id);
            dishFlavorMapper.deleteByDishId(id);
        });

    }

    /**
     * @Description:根据id查询菜品
     * @return: com.sky.result.Result<com.sky.vo.DishVO>
     * @author: chen
     * @date: 2023/7/12 21:42
     */
    @Override
    public DishVO getById(Long id) {
        Dish dish = dishMapper.getByid(id);
        List<DishFlavor> flavors=dishFlavorMapper.getByDishId(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    /**
     * @Description:修改菜品
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/12 21:52
     */
    @Override
    public void update(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors!=null&&flavors.size()>0){
            dishFlavorMapper.deleteByDishId(dishDTO.getId());
            flavors.forEach(flavor -> {
                flavor.setDishId(dishDTO.getId());
            });
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * @Description:菜品的起售、停售
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/12 23:02
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Dish dish = Dish.builder().status(status).id(id).build();
        dishMapper.update(dish);
        if (StatusConstant.DISABLE==status){
            ArrayList<Long> dishIds = new ArrayList<>();
            dishIds.add(id);
            List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(dishIds);
            if (setmealIds!=null&&setmealIds.size()>0){
                setmealIds.forEach(setmealId->{
                    Setmeal setmeal = Setmeal.builder().id(setmealId).status(StatusConstant.DISABLE).build();
                    setmealMapper.update(setmeal);
                });
            }
        }
    }

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }
        return dishVOList;
    }

    /**
     * @Description:根据分类查询菜品
     * @return: java.util.List<com.sky.entity.Dish>
     * @author: chen
     * @date: 2023/7/14 13:46
     */
    @Override
    public List<Dish> list(Long categoryId) {
        Dish dish = Dish.builder().categoryId(categoryId).status(StatusConstant.ENABLE).build();
        return dishMapper.list(dish);
    }
}
