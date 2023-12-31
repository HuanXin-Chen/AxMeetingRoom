package github.ax.meeting.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import github.ax.meeting.entities.*;
import github.ax.meeting.mapper.ApplicationRecordMapper;
import github.ax.meeting.mapper.DepartmentMapper;
import github.ax.meeting.mapper.RoomMapper;
import github.ax.meeting.mq.EmailInitListener;
import github.ax.meeting.mq.EmailSendEvent;
import github.ax.meeting.mq.EventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 审核层服务
@Service
public class ApplicationRecordService {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ApplicationRecordMapper applicationRecordMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private EventPublisher eventPublisher;

    //提交申请
    public Msg addApply(Map<String, Object> para, Date applyDate, Integer applySlot) {
        Integer deptId = (Integer) para.get("deptId");
        Integer roomId = (Integer) para.get("roomId");
        String meetingTheme = (String)para.get("meetingTheme");
        Integer meetingSize = (Integer) para.get("roomSize");
        //获取当前时间转化为字符串
        long l = System.currentTimeMillis();
        Date applyTime=new Date(l);

        //封装对象
        ApplicationRecord applicationRecord = new ApplicationRecord(deptId,roomId,applyTime,applyDate,applySlot,meetingTheme,meetingSize);
        applicationRecord.setAuditStatus(0);
        applicationRecordMapper.insert(applicationRecord);

        Department department = departmentMapper.selectByPrimaryKey(deptId);
        Room room = roomMapper.selectByPrimaryKey(roomId);
        String email = department.getDeptPhone();
        String room_no = room.getRoomNo();
        String time = applyDate.toString();

        Map<String,Object>  map = new HashMap<>();
        map.put("email",email);
        map.put("room_no",room_no);
        map.put("time",time);

        eventPublisher.publish("chx-mq-init", EmailSendEvent.create(map));

        return Msg.success();
    }

//    public Msg duplicateCheck(Map<String, Object> para) {
//        Msg msg = departmentService.selectCondition(para);
//        return  msg;
//    }

    //查看所有申请
    public Msg select(Map<String, Object> para) {
        Integer auditStatus = null;
        if (para.containsKey("auditStatus")){
            auditStatus = Integer.parseInt((String) para.get("auditStatus"));
        }
        Integer deptId = null;
        if(para.containsKey("deptId")){    //判断是否管理员
            deptId = (Integer) para.get("deptId");
        }
        Integer pageSize = Integer.parseInt((String) para.get("pageSize"));
        Integer currentPage = Integer.parseInt((String)para.get("currentPage"));
        ApplicationRecord applicationRecord = new ApplicationRecord();
        applicationRecord.setDeptId(deptId);
        applicationRecord.setAuditStatus(auditStatus);
        PageHelper.startPage(currentPage,pageSize);
        List<ApplicationRecord>  applicationRecords = applicationRecordMapper.selectByCondition(applicationRecord);
        PageInfo info = new PageInfo(applicationRecords);
        Map<String,Object> map = new HashMap<>();
        map.put("result",info);
        return Msg.success().add(map);
    }

    //根据id查对象
    public ApplicationRecord getById(Integer id) {
        return applicationRecordMapper.selectByPrimaryKey(id);
    }

    //通过申请
    public Msg updateStatus(Integer id,Integer auditStatus,String reason) {
        //审批时间未当前系统时间
        long l = System.currentTimeMillis();
        Date auditTime = new Date(l);
        ApplicationRecord applicationRecord = new ApplicationRecord();
        applicationRecord.setRejectReason(reason);
        //修改状态为通过
        applicationRecord.setApplicationId(id);
        applicationRecord.setAuditStatus(auditStatus);
        applicationRecord.setAuditTime(auditTime);
        int size = applicationRecordMapper.updateByPrimaryKey(applicationRecord);

        ApplicationRecord applicationRecordEmail = applicationRecordMapper.selectByPrimaryKey(id);
        Department department = departmentMapper.selectByPrimaryKey(applicationRecordEmail.getDeptId());
        Room room = roomMapper.selectByPrimaryKey(applicationRecordEmail.getRoomId());
        String email = department.getDeptPhone();
        String room_no = room.getRoomNo();
        String time = applicationRecordEmail.getApplyTime().toString();
        String reasonTo = reason;
        String status = auditStatus.toString();

        Map<String,Object>  map = new HashMap<>();
        map.put("email",email);
        map.put("room_no",room_no);
        map.put("time",time);
        map.put("reason",reasonTo);
        map.put("status",status);

        eventPublisher.publish("chx-mq-end", EmailSendEvent.create(map));

        return size>0?Msg.success():Msg.fault();
    }

    //查出占用的会议室
    public List<TempStatus> selectOccupy(Date date) {
        //得到被占用的会议室以及时间段
        return applicationRecordMapper.selectOccupy(date);
    }

    public Msg revoke(Integer id) {
        applicationRecordMapper.deleteByPrimaryKey(id);
        return Msg.success();
    }
}
