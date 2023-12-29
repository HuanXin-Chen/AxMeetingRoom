package github.ax.meeting.mapper;

import github.ax.meeting.entities.Room;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface RoomMapper {

    int deleteByPrimaryKey(Integer roomId);


    int insert(Room record);


    Room selectByPrimaryKey(Integer roomId);


    List<Room> selectAll();


    int updateByPrimaryKey(Room record);

    List<Room> selectByNo(Room record);

    Room duplicateCheck(Room room);

    void updateEquipment(@Param("roomId") Integer id, @Param("air") Boolean air,@Param("projector")Boolean projector);
}