package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * @Description:批量添加口味信息
     * @return: void
     * @author: chen
     * @date: 2023/7/12 16:30
     */
    void insertBatch(List<DishFlavor> flavors);
}
