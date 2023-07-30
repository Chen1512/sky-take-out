package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shkstart
 * @create 2023--28-15:13
 */
@Mapper
public interface ShoppingCartMapper {
    /**
     * 条件查询
     *
     * @param shoppingCart
     * @return
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 更新商品数量
     *
     * @param shoppingCart
     */
    @Update("update shopping_cart set number = #{number} where id = #{id}")
    void updateNumberById(ShoppingCart shoppingCart);

    /**
     * 插入购物车数据
     *
     * @param shoppingCart
     */
    @Insert("insert into shopping_cart (name, user_id, dish_id, setmeal_id, dish_flavor, number, amount, image, create_time) " +
            " values (#{name},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{number},#{amount},#{image},#{createTime})")
    void insert(ShoppingCart shoppingCart);

    /**
     * @Description:删除购物车
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/28 15:58
     */
    @Delete("delete from shopping_cart where id=#{id}")
    void deleteById(Long id);

    /**
     * @Description:清空购物车
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/28 16:16
     */
    @Delete("delete from shopping_cart where user_id=#{userId}")
    void deleteByUserId(Long userId);

    /**
     * @Description:再来一单
     * @return: void
     * @author: chen
     * @date: 2023/7/30 14:13
     */
    void insertBatch(ArrayList<ShoppingCart> shoppingCarts);
}
