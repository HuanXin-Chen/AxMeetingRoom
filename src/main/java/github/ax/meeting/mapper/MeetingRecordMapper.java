package github.ax.meeting.mapper;

import github.ax.meeting.entities.MeetingRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
@Mapper
public interface MeetingRecordMapper {

    int deleteByPrimaryKey(Integer recordId);


    int insert(MeetingRecord record);


    MeetingRecord selectByPrimaryKey(Integer recordId);


    List<MeetingRecord> selectAll();


    int updateByPrimaryKey(MeetingRecord record);


    List<MeetingRecord> selectByCondition(MeetingRecord record,Integer pass);

    List<MeetingRecord> selectByConditionFree(Date meetinDate);

    int deleteByCondition(@Param("roomId") Integer id);

    int deleteByDeptId(@Param("deptId") Integer id);
}