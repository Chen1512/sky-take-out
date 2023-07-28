package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * @author shkstart
 * @create 2023--12-15:57
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * @Description:添加菜品
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/12 15:59
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        dishService.saveWithFlavor(dishDTO);
        cleanCache("dish_"+dishDTO.getCategoryId());
        return Result.success();
    }

    /**
     * @Description:菜品分页查询
     * @return: com.sky.result.Result<com.sky.vo.DishVO>
     * @author: chen
     * @date: 2023/7/12 17:31
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        PageResult pageResult= dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**批量删除菜品
     * @Description:
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/12 18:53
     */
    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result delete(@RequestParam List<Long> ids){
        dishService.deleteBatch(ids);
        cleanCache("dish_*");
        return Result.success();
    }

    /**
     * @Description:根据id查询菜品
     * @return: com.sky.result.Result<com.sky.vo.DishVO>
     * @author: chen
     * @date: 2023/7/12 21:42
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> getById(@PathVariable Long id){
        DishVO dishVO=dishService.getById(id);
        return Result.success(dishVO);
    }

    /**
     * @Description:修改菜品
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/12 21:52
     */
    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishDTO){
        dishService.update(dishDTO);
        cleanCache("dish_*");
        return Result.success();
    }

    /**
     * @Description:菜品的起售、停售
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/12 23:02
     */
    @PostMapping("/status/{status}")
    @ApiOperation("菜品的起售、停售")
    public Result startOrStop(@PathVariable Integer status,Long id){
        dishService.startOrStop(status,id);
        cleanCache("dish_*");
        return Result.success();
    }


    /**
     * @Description:根据分类查询菜品
     * @return: com.sky.result.Result<java.util.List<com.sky.entity.Dish>>
     * @author: chen
     * @date: 2023/7/14 13:45
     */
    @GetMapping("/list")
    @ApiOperation("根据分类查询菜品")
    public Result<List<Dish>> list(Long categoryId){
        List<Dish> list=dishService.list(categoryId);
        return Result.success(list);
    }

    /**
     * @Description:清理缓存
     * @return: void
     * @author: chen
     * @date: 2023/7/28 9:42
     */
    private void cleanCache(String pattern){
        Set keys = stringRedisTemplate.keys(pattern);
        stringRedisTemplate.delete(keys);
    }
}
