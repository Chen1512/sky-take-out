package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * @Description:新增员工
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/9 16:10
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * @Description:分页查询
     * @return: com.sky.result.PageResult
     * @author: chen
     * @date: 2023/7/10 13:56
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * @Description:启用禁用员工账号
     * @return: void
     * @author: chen
     * @date: 2023/7/10 13:57
     */
    void startOrStop(Integer status, Long id);
}
