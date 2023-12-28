package github.ax.meeting.mapper;

import github.ax.meeting.entities.MeetingRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MeetingRecordMapper {

    int deleteByPrimaryKey(Integer recordId);


    int insert(MeetingRecord record);


    MeetingRecord selectByPrimaryKey(Integer recordId);


    List<MeetingRecord> selectAll();


    int updateByPrimaryKey(MeetingRecord record);


    List<MeetingRecord> selectByCondition(MeetingRecord record,Integer pass);

    int deleteByCondition(@Param("roomId") Integer id);

    int deleteByDeptId(@Param("deptId") Integer id);
}