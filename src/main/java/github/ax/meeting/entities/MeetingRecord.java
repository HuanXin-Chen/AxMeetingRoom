package github.ax.meeting.entities;

import java.util.Date;

public class MeetingRecord {

    private Integer recordId;


    private String meetingTheme;


    private Integer meetingSize;


    private Integer deptId;


    private Integer roomId;


    private Date meetingDate;


    private Integer meetingSlot;

    private Integer applyId;

    public Integer getApplyId() {
        return applyId;
    }

    public void setApplyId(Integer applyId) {
        this.applyId = applyId;
    }

    private String deptName;
    private String roomNo;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }


    public Integer getRecordId() {
        return recordId;
    }


    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }


    public String getMeetingTheme() {
        return meetingTheme;
    }


    public void setMeetingTheme(String meetingTheme) {
        this.meetingTheme = meetingTheme == null ? null : meetingTheme.trim();
    }


    public Integer getMeetingSize() {
        return meetingSize;
    }


    public void setMeetingSize(Integer meetingSize) {
        this.meetingSize = meetingSize;
    }


    public Integer getDeptId() {
        return deptId;
    }


    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Date getMeetingDate() {
        return meetingDate;
    }


    public void setMeetingDate(Date meetingDate) {
        this.meetingDate = meetingDate;
    }


    public Integer getMeetingSlot() {
        return meetingSlot;
    }


    public void setMeetingSlot(Integer meetingSlot) {
        this.meetingSlot = meetingSlot;
    }

    public MeetingRecord() {
    }

    public MeetingRecord(String meetingTheme, Integer meetingSize, Integer deptId, Integer roomId, Date meetingDate, Integer meetingSlot) {
        this.meetingTheme = meetingTheme;
        this.meetingSize = meetingSize;
        this.deptId = deptId;
        this.roomId = roomId;
        this.meetingDate = meetingDate;
        this.meetingSlot = meetingSlot;
    }
}