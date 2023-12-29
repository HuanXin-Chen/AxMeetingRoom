package github.ax.meeting.controller;

import github.ax.meeting.annotation.AccessInterceptor;
import github.ax.meeting.entities.Msg;
import github.ax.meeting.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    @AccessInterceptor(key = "ipAddress", fallbackMethod = "loginErr", permitsPerSecond = 1.0d, blacklistCount = 10)
    public Msg login(HttpServletRequest request, @RequestBody Map<String , Object> para){
        String ipAddress = request.getRemoteAddr();
        return loginService.login(para);
    }

    public Msg loginErr(HttpServletRequest request, @RequestBody  Map<String , Object> para) {
        Msg msg = Msg.fault();
        msg.setMessage("频次限制:请勿恶意访问");
        return msg;
    }

}
