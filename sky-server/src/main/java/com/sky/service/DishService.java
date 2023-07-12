package com.sky.service;

import com.sky.dto.DishDTO;

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
}
