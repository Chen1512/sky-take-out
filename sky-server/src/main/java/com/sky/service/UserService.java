package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * @author shkstart
 * @create 2023--13-21:09
 */
public interface UserService {
    /**
     * @Description:微信登录
     * @return: com.sky.entity.User
     * @author: chen
     * @date: 2023/7/13 21:28
     */
    User wxLogin(UserLoginDTO userLoginDTO);
}
