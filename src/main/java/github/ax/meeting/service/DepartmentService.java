package github.ax.meeting.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import github.ax.meeting.entities.Department;
import github.ax.meeting.entities.Msg;
import github.ax.meeting.mapper.ApplicationRecordMapper;
import github.ax.meeting.mapper.DepartmentMapper;
import github.ax.meeting.mapper.MeetingRecordMapper;
import github.ax.meeting.util.Const;
import github.ax.meeting.util.RedissonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//部门层服务
@Service
public class DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private MeetingRecordMapper meetingRecordMapper;

    @Autowired
    private ApplicationRecordMapper applicationRecordMapper;

    @Autowired
    private RedissonService redissonService;


    public Msg getDept(Map<String,Object> para) {
        Integer id = Integer.parseInt((String)para.get("deptId")) ;
        String key = Const.Redis_DEPT + id;
        Department department = redissonService.getValue(key);
        if(department == null){
            department = departmentMapper.selectByPrimaryKey(id);
            redissonService.setValue(key,department);
        }
        Map<String ,Object> map = new HashMap<>();
        map.put("result",department);
        return department!=null?Msg.success().add(map):Msg.fault();
    }

    public Msg getAll(Map<String,Object> para) {
        List<Department> departments;
        Map<String,Object> map = new HashMap<>();
        if(para.containsKey("currentPage")){
            Integer currentPage = Integer.parseInt((String)para.get("currentPage"));
            Integer pageSize = Integer.parseInt((String)para.get("pageSize"));
            PageHelper.startPage(currentPage,pageSize);
            departments= departmentMapper.selectAll();
            PageInfo pageInfo = new PageInfo(departments);
            map.put("result",pageInfo);
        }else{
            departments= departmentMapper.selectAll();
            map.put("result",departments);
        }
        return Msg.success().add(map);
    }

    public Msg update(Map<String,Object> para) {
        Integer id = (Integer)para.get("deptId");

        String key = Const.Redis_DEPT+id;
        Department department = redissonService.getValue(key);
        if(department!=null) redissonService.deleteValue(key);

        String deptName = null;
        String deptPhone = null;
        String deptNo = null;
        String deptPassword = null;
        if(para.containsKey("deptName"))
            deptName = (String)para.get("deptName");
        if(para.containsKey("deptPhone"))
            deptPhone = (String)para.get("deptPhone");
        if(para.containsKey("deptNo"))
            deptNo = (String)para.get("deptNo");
        if(para.containsKey("deptPassword"))
            deptPassword = (String)para.get("deptPassword");
        department = new Department(deptName,deptPhone,deptNo,deptPassword);
        department.setDeptId(id);
        int num = departmentMapper.updateByPrimaryKey(department);
        return num>0?Msg.success():Msg.fault();
    }

    public Msg addDept(Map<String,Object> para) {
        String deptName = (String)para.get("deptName");
        String deptPhone = (String)para.get("deptPhone");
        String deptNo = (String)para.get("deptNo");
        String deptPassword = (String)para.get("deptPassword");
        Department department = new Department(deptName,deptPhone,deptNo,deptPassword);
        int num = departmentMapper.insert(department);
        return num>0?Msg.success():Msg.fault();
    }

    public Msg deleteById(@RequestBody Map<String,Object> para) {
        Integer id = (Integer)para.get("deptId");

        String key = Const.Redis_DEPT+id;
        Department department = redissonService.getValue(key);
        if(department!=null) redissonService.deleteValue(key);

        int num = departmentMapper.deleteByPrimaryKey(id);
        applicationRecordMapper.deleteByDeptId(id);
        meetingRecordMapper.deleteByDeptId(id);
        return num>0?Msg.success():Msg.fault();
    }

    public Msg selectCondition(Map<String, Object> para) {
        Integer currentPage = Integer.parseInt((String)para.get("currentPage"));
        Integer pageSize = Integer.parseInt((String)para.get("pageSize"));
        String deptName = null;
        String deptPhone = null;
        String deptNo = null;
        if(para.containsKey("deptName"))
            deptName = "%"+para.get("deptName")+"%";
        if(para.containsKey("deptPhone"))
            deptPhone= "%"+para.get("deptPhone")+"%";
        if(para.containsKey("deptNo"))
            deptNo = "%"+para.get("deptNo")+"%";
        Department department = new Department(deptName,deptPhone,deptNo,null);
        PageHelper.startPage(currentPage,pageSize);
        List<Department> departments = departmentMapper.selectByCondition(department);
        PageInfo pageInfo = new PageInfo(departments);
        Map<String , Object> map = new HashMap<>();
        map.put("result",pageInfo);

        return departments!=null?Msg.success().add(map):Msg.fault();
    }

    public Msg duplicateCheck(Map<String ,Object>para){
        String deptName = (String) para.get("deptName");
        String deptNo= (String) para.get("deptNo");
        Department department = new Department(deptName,null,deptNo,null);
        if (para.containsKey("deptId"))
            department.setDeptId((Integer)para.get("deptId"));
        List<Department> departments = departmentMapper.duplicateCheck(department);
        if(departments.size()==0){
            return Msg.success();
        }else if(departments.size()==2){
            return Msg.fault().setError("部门名称和部门编号都重复");
        }else{
            String error ="";
            boolean isone = false;
            if(departments.get(0).getDeptName().equals(deptName)){
                error += "部门名称重复";
                isone = true;
            }
            if(departments.get(0).getDeptNo().equals(deptNo)){
                error +=  isone?"以及部门编号重复":"编号部门重复";
            }
            return Msg.fault().setError(error);
        }

    }


    //通过no查id
    public int seletByNo(String no){
         Department department =departmentMapper.seletByNo(no);
         return department.getDeptId();
    }
}
