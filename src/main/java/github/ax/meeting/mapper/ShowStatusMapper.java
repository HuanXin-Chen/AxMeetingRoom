package github.ax.meeting.mapper;

import github.ax.meeting.entities.Room;
import github.ax.meeting.entities.ShowStatus;
import github.ax.meeting.entities.TempStatus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface ShowStatusMapper {

    int deleteByPrimaryKey(Integer id);


    int insert(Room room);



    ShowStatus selectByPrimaryKey(Integer id);


    List<ShowStatus> selectAll();


    int updateByPrimaryKey(ShowStatus record);

    void updateStatus(@Param("status") TempStatus occupyRooms);

    void updateDefault(@Param("room") Room room);

    void updateRepair(@Param("room") Room room);

    void updateEquipment(@Param("roomId") Integer id, @Param("air") Boolean air,@Param("projector")Boolean projector);
}