package github.ax.meeting.controller;

import github.ax.meeting.annotation.AccessInterceptor;
import github.ax.meeting.entities.Msg;
import github.ax.meeting.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class ManagerController {
    @Autowired
    private ManagerService managerService;

    @PostMapping("/manager")
    @AccessInterceptor(key = "ipAddress", fallbackMethod = "loginErr", permitsPerSecond = 1.0d, blacklistCount = 10)
    public Msg login(HttpServletRequest httpServletRequest, @RequestBody Map<String, Object> para){
        String ipAddress = httpServletRequest.getRemoteAddr();
        return managerService.login(para);
    }

    public Msg loginErr(HttpServletRequest request, @RequestBody  Map<String , Object> para) {
        Msg msg = Msg.fault();
        msg.setMessage("频次限制:请勿恶意访问");
        return msg;
    }
}
