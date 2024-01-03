package github.ax.meeting.entities;

import java.util.Date;

//申请实体
public class ApplicationRecord {

    private Integer applicationId;


    private Integer deptId;


    private Integer roomId;

    private Date applyTime;


    private Date auditTime;


    private Integer auditStatus;


    private String rejectReason;


    private Date applyDate;


    private Integer applySlot;


    private String meetingTheme;


    private Integer meetingSize;

    private String roomNo;
    private String deptName;

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
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


    public Date getApplyTime() {
        return applyTime;
    }


    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }


    public Date getAuditTime() {
        return auditTime;
    }


    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }


    public Integer getAuditStatus() {
        return auditStatus;
    }


    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }


    public String getRejectReason() {
        return rejectReason;
    }


    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason == null ? null : rejectReason.trim();
    }


    public Date getApplyDate() {
        return applyDate;
    }


    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public Integer getApplySlot() {
        return applySlot;
    }


    public void setApplySlot(Integer applySlot) {
        this.applySlot = applySlot;
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

    public ApplicationRecord() {
    }

    public ApplicationRecord(Integer deptId, Integer roomId, Date applyTime, Date applyDate, Integer applySlot, String meetingTheme, Integer meetingSize) {
        this.deptId = deptId;
        this.roomId = roomId;
        this.applyTime = applyTime;
        this.applyDate = applyDate;
        this.applySlot = applySlot;
        this.meetingTheme = meetingTheme;
        this.meetingSize = meetingSize;
    }
}