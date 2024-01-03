package github.ax.meeting.entities;

//会议室实体
public class Room {

    private Integer roomId;


    private String roomNo;


    private Integer roomSize;


    private Boolean roomStatus;

    private Boolean tempStatus;

    private Boolean air;
    private Boolean projector;

    public Boolean getAir() {
        return air;
    }

    public void setAir(Boolean air) {
        this.air = air;
    }

    public Boolean getProjector() {
        return projector;
    }

    public void setProjector(Boolean projector) {
        this.projector = projector;
    }

    public Boolean getTempStatus() {
        return tempStatus;
    }

    public void setTempStatus(Boolean tempStatus) {
        this.tempStatus = tempStatus;
    }


    public Integer getRoomId() {
        return roomId;
    }


    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }


    public String getRoomNo() {
        return roomNo;
    }


    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo == null ? null : roomNo.trim();
    }


    public Integer getRoomSize() {
        return roomSize;
    }


    public void setRoomSize(Integer roomSize) {
        this.roomSize = roomSize;
    }


    public Boolean getRoomStatus() {
        return roomStatus;
    }


    public void setRoomStatus(Boolean roomStatus) {
        this.roomStatus = roomStatus;
    }

    public Room() {
    }

    public Room(String roomNo, Integer roomSize, Boolean roomStatus) {
        this.roomNo = roomNo;
        this.roomSize = roomSize;
        this.roomStatus = roomStatus;
    }
}