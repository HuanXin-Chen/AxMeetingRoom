package github.ax.meeting.mapper;

import github.ax.meeting.entities.Department;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DepartmentMapper {

    int deleteByPrimaryKey(Integer deptId);


    int insert(Department record);


    Department selectByPrimaryKey(Integer deptId);


    List<Department> selectAll();


    int updateByPrimaryKey(Department record);

    //部门登录信息
    Department loginMessage(Department record);

    List<Department> selectByCondition(Department record);

    List<Department> duplicateCheck(Department record);
    //通过编号查id
    Department seletByNo(String No);
}