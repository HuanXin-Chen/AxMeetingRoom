package github.ax.meeting.entities;

//状态实体
public class TempStatus {
    private int roomId;
    private int meetingSlot;

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getMeetingSlot() {
        return meetingSlot;
    }

    public void setMeetingSlot(int meetingSlot) {
        this.meetingSlot = meetingSlot;
    }
}
