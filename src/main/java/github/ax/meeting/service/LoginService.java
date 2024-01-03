package github.ax.meeting.service;

import github.ax.meeting.entities.Department;
import github.ax.meeting.entities.Msg;
import github.ax.meeting.mapper.DepartmentMapper;
import github.ax.meeting.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

// 登录层服务
@Service
public class LoginService {
    @Autowired
    private DepartmentMapper departmentMapper;

    public Msg login(Map<String , Object> para){
        String name = (String)para.get("username");
        String pwd = (String)para.get("password");
        Department department=new Department();
        department.setDeptNo(name);
        department.setDeptPassword(pwd);
        Department b = departmentMapper.loginMessage(department);
        if (b!=null){
            String token = TokenUtil.sign(b);
            Map<String,Object> map = new HashMap<>();
            map.put("token",token);
            return Msg.success().add(map);
        }
        return Msg.fault();
    }
}
