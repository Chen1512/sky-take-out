package com.sky.controller.user;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @author shkstart
 * @create 2023--13-10:51
 */
@RestController("userShopController")
@RequestMapping("/user/shop")
@Api(tags = "店铺相关接口")
public class ShopController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public static final String KEY = "SHOP_STATUS";


    /**
     * @Description:获取店铺营业状态
     * @return: com.sky.result.Result<java.lang.Integer>
     * @author: chen
     * @date: 2023/7/13 11:06
     */
    @GetMapping("/status")
    @ApiOperation("获取店铺营业状态")
    public Result<Integer> getStatus(){
        Integer status= Integer.valueOf(stringRedisTemplate.opsForValue().get(KEY));
        return Result.success(status);
    }
}
