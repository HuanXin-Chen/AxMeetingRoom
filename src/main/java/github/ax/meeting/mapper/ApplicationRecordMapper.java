package github.ax.meeting.mapper;

import github.ax.meeting.entities.ApplicationRecord;
import github.ax.meeting.entities.TempStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface ApplicationRecordMapper {

    int deleteByPrimaryKey(Integer applicationId);


    int insert(ApplicationRecord record);


    ApplicationRecord selectByPrimaryKey(Integer applicationId);


    List<ApplicationRecord> selectAll();


    int updateByPrimaryKey(ApplicationRecord record);

    List<ApplicationRecord> selectByCondition(ApplicationRecord record);

    List<TempStatus> selectOccupy(@Param("date") Date date);

    int deleteByDeptId(@Param("deptId") Integer id);
}