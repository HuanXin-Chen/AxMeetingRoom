package github.ax.meeting.controller;

import github.ax.meeting.annotation.LoginCharacter;
import github.ax.meeting.entities.ApplicationRecord;
import github.ax.meeting.entities.Msg;
import github.ax.meeting.entities.Room;
import github.ax.meeting.entities.TempStatus;
import github.ax.meeting.service.*;
import github.ax.meeting.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

// 应用Api接口
@RestController
public class ApplyController {

    @Autowired
    private ApplicationRecordService applicationRecordService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ShowStatusService showStatusService;

    @Autowired
    private MeetingRecordService meetingRecordService;

    //查询时会覆盖
    private Date applyDate;
    private Integer  applySlot;
    @LoginCharacter(name = "user")
    @GetMapping("/apply/free")   //pageSize currentPage  applyDate
    //查出该日期所有会议室日期的信息
    public Msg selectFreeByTime(@RequestParam Map<String ,Object> para) throws ParseException {
        String time = (String) para.get("applyDate");
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        applyDate = ft.parse(time);
        //更新临时表为原始状态
        showStatusService.updateDefault();
        //查询会议室的维修状态
        List<Room> rooms = roomService.getRooms();
        //将不可用状态写入临时表
        for(Room room : rooms){
            if (room.getRoomStatus()==false){
                showStatusService.updateRepair(room);
            }
        }
        //查出占用的会议室
        List<TempStatus> occupyRooms =  applicationRecordService.selectOccupy(applyDate);
        //将不可用状态写入临时表
        for(TempStatus tempStatus : occupyRooms){
            showStatusService.updateOccupy(tempStatus);
        }
        //返回结果
        return roomService.getFree(para);
    }

    //申请会议室
    @LoginCharacter(name = "user")
    @PostMapping("/apply")
    public Msg applyRoom(@RequestBody Map<String ,Object>para ,@RequestHeader Map<String,Object> header) throws ParseException {
        String token = (String) header.get("authorization");
        String deptNo = TokenUtil.getNo(token);
        String time = (String) para.get("applyDate");
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        applyDate = ft.parse(time);
        applySlot = (Integer) para.get("applySlot");
        int deptId = departmentService.seletByNo(deptNo);
        para.put("deptId",deptId);
        return applicationRecordService.addApply(para,applyDate,applySlot);
    }

    //部门取消申请（不分状态）  application_id
    @LoginCharacter(name = "user")
    @DeleteMapping("/apply/revoke")
    public Msg deleteApply(@RequestBody Map<String,Object> para){
        Integer id = (Integer) para.get("applicationId");
        ApplicationRecord applicationRecord = applicationRecordService.getById(id);
        if (applicationRecord.getAuditStatus()==1){
            //撤销已经通过的申请
            meetingRecordService.revoke(id);
        }
        return applicationRecordService.revoke(id);
    }


}
