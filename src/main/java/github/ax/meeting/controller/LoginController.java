package github.ax.meeting.controller;

import github.ax.meeting.entities.Msg;
import github.ax.meeting.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public Msg login(@RequestBody Map<String , Object> para){
        return loginService.login(para);
    }

}
