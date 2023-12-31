package github.ax.meeting.mapper;

import github.ax.meeting.entities.Manager;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface ManagerMapper {

    int deleteByPrimaryKey(String username);


    int insert(Manager record);


    Manager selectByPrimaryKey(String username);

    List<Manager> selectAll();


    int updateByPrimaryKey(Manager record);

    //部门登录信息
    Manager loginMessage(Manager record);
}