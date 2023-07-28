package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author shkstart
 * @create 2023--14-13:05
 */
@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * @Description:添加套餐
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/14 13:11
     */
    @PostMapping
    @ApiOperation("添加套餐")
    public Result save(@RequestBody SetmealDTO setmealDTO){
        setmealService.save(setmealDTO);
        return Result.success();
    }

    /**
     * @Description:分页查询
     * @return: com.sky.result.Result<com.sky.result.PageResult>
     * @author: chen
     * @date: 2023/7/14 14:31
     */
    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO){
        PageResult pageResult=setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * @Description:批量删除套餐
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/14 14:53
     */
    @DeleteMapping
    @ApiOperation("批量删除套餐")
    public Result delete(@RequestParam List<Long> ids){
        setmealService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * @Description:根据id查询套餐
     * @return: com.sky.result.Result<com.sky.vo.SetmealVO>
     * @author: chen
     * @date: 2023/7/14 15:09
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询套餐")
    public Result<SetmealVO> getById(@PathVariable Long id){
        SetmealVO setmealVO=setmealService.getById(id);
        return Result.success(setmealVO);
    }

    /**
     * @Description:修改套餐
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/14 15:21
     */
    @PutMapping
    @ApiOperation("修改套餐")
    public Result update(@RequestBody SetmealDTO setmealDTO){
        setmealService.update(setmealDTO);
        return Result.success();
    }

    /**
     * @Description:套餐起售、停售
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/14 15:35
     */
    @PostMapping("/status/{status}")
    @ApiOperation("套餐起售、停售")
    public Result startOrStop(@PathVariable Integer status,Long id){
        setmealService.startOrStop(status,id);
        return Result.success();
    }
}
