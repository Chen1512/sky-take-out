package com.sky.mapper;

import com.sky.entity.Setmeal;
import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.Map;

@Mapper
public interface UserMapper {
    /**
     * @Description:微信登录
     * @return: com.sky.entity.User
     * @author: chen
     * @date: 2023/8/1 16:47
     */
    @Select("select * from user where openid=#{openid}")
    User getByOpenid(String openid);

    /**
     * @Description:微信登录
     * @return: com.sky.entity.User
     * @author: chen
     * @date: 2023/8/1 16:47
     */
    void insert(User user);

    /**
     * @Description:订单支付
     * @return: com.sky.entity.User
     * @author: chen
     * @date: 2023/8/1 16:48
     */
    @Select("select * from user where id=#{userId}")
    User getById(Long userId);

    /**
     * @Description:根据时间区间统计指定状态的订单数量
     * @return: java.lang.Integer
     * @author: chen
     * @date: 2023/8/1 16:48
     */
    Integer countByMap(Map map);
}
