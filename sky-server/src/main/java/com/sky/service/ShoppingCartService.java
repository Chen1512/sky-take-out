package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

/**
 * @author shkstart
 * @create 2023--28-14:40
 */
public interface ShoppingCartService {
    /**
     * @Description:添加购物车
     * @return: void
     * @author: chen
     * @date: 2023/7/28 15:43
     */
    void addShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * @Description:查看购物车
     * @return: void
     * @author: chen
     * @date: 2023/7/28 15:43
     */
    List<ShoppingCart> showShoppingCart();

}
