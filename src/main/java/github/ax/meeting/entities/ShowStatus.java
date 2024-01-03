package github.ax.meeting.entities;

//状态实体
public class ShowStatus {

    private Integer roomId;


    private String roomNo;


    private int roomSize;

    public ShowStatus(Integer id, Integer size) {
        this.roomId =id;
        this.roomSize=size;
    }

    public ShowStatus() {
    }

    public int getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(int roomSize) {
        this.roomSize = roomSize;
    }


    private boolean air;
    private boolean projector;

    public boolean isAir() {
        return air;
    }

    public void setAir(boolean air) {
        this.air = air;
    }

    public boolean isProjector() {
        return projector;
    }

    public void setProjector(boolean projector) {
        this.projector = projector;
    }


    private Boolean one;


    private Boolean two;


    private Boolean three;


    private Boolean four;


    private Boolean five;

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


    public Boolean getOne() {
        return one;
    }


    public void setOne(Boolean one) {
        this.one = one;
    }


    public Boolean getTwo() {
        return two;
    }


    public void setTwo(Boolean two) {
        this.two = two;
    }


    public Boolean getThree() {
        return three;
    }


    public void setThree(Boolean three) {
        this.three = three;
    }


    public Boolean getFour() {
        return four;
    }


    public void setFour(Boolean four) {
        this.four = four;
    }


    public Boolean getFive() {
        return five;
    }


    public void setFive(Boolean five) {
        this.five = five;
    }
}