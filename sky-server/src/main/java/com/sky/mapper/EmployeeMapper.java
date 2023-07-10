package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeeDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * @Description:新增员工
     * @return: com.sky.result.Result
     * @author: chen
     * @date: 2023/7/9 16:10
     */
    @Insert("insert into employee(name, username, password, phone, sex, id_number, create_time, update_time, create_user, update_user)" +
            "value " +
            "(#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    @AutoFill(OperationType.INSERT)
    void insert(Employee employee);

    /**
     * @Description:分页查询
     * @return: com.github.pagehelper.Page<com.sky.entity.Employee>
     * @author: chen
     * @date: 2023/7/10 14:03
     */
    Page<Employee> pageQuery(String name);

    /**
     * @Description:启用禁用员工账号
     * @return: void
     * @author: chen
     * @date: 2023/7/10 14:03
     */
    @AutoFill(OperationType.UPDATE)
    void update(Employee employee);

    /**
     * @Description:根据id查询员工
     * @return: com.sky.entity.Employee
     * @author: chen
     * @date: 2023/7/10 14:35
     */
    @Select("select * from employee where id=#{id}")
    Employee getById(Long id);

}
