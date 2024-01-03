package github.ax.meeting.controller;

import github.ax.meeting.annotation.AccessInterceptor;
import github.ax.meeting.entities.Msg;
import github.ax.meeting.service.AiChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

// 机器人Api接口

@Slf4j
@RestController
public class AiHelpController {

    @Autowired
    private AiChatService aiChatService;

    @GetMapping("/ai/help")
    @AccessInterceptor(key = "ipAddress", fallbackMethod = "helpErr", permitsPerSecond = 1.0d, blacklistCount = 10)
    public Msg help(HttpServletRequest httpServletRequest, @RequestHeader Map<String, Object> header) throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = aiChatService.completions();
        String reponse = future.join();
        log.info("请求结果:{}",reponse);
        Msg msg = new Msg();
        msg.setCode(200);
        msg.setMessage("想好了呢！" + reponse);
        return msg;
    }

    public Msg helpErr(HttpServletRequest request, @RequestBody Map<String , Object> para) {
        Msg msg = Msg.fault();
        msg.setMessage("让我想想哦，请稍等！");
        return msg;
    }
}
