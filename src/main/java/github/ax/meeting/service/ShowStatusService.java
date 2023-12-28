package github.ax.meeting.service;

import github.ax.meeting.entities.Room;
import github.ax.meeting.entities.ShowStatus;
import github.ax.meeting.entities.TempStatus;
import github.ax.meeting.mapper.RoomMapper;
import github.ax.meeting.mapper.ShowStatusMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowStatusService {

    @Autowired
    private ShowStatusMapper showStatusMapper;

    @Autowired
    private RoomMapper roomMapper;

    public void updateOccupy(TempStatus occupyRooms) {
        showStatusMapper.updateStatus(occupyRooms);
    }

    public void updateDefault() {
        List<Room> rooms = roomMapper.selectAll();
        for(Room room : rooms){
            showStatusMapper.updateDefault(room);
        }
    }

    public void updateMessage(ShowStatus showStatus) {
        showStatusMapper.updateByPrimaryKey(showStatus);
    }

    public void updateRepair(Room room) {
        showStatusMapper.updateRepair(room);
    }
}
