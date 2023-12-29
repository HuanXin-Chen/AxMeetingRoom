package github.ax.meeting.controller;

import github.ax.meeting.entities.Msg;
import github.ax.meeting.util.TokenUtil;
import github.huanxin.chatgpt.model.ChatCompletionRequest;
import github.huanxin.chatgpt.model.Model;
import github.huanxin.chatgpt.model.Role;
import github.huanxin.chatgpt.session.OpenAiSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@RestController
public class AiHelpController {

    @Autowired(required = false)
    private OpenAiSession openAiSession;

    @GetMapping("/ai/help")
    public String history( @RequestHeader Map<String,Object> header) throws InterruptedException, ExecutionException {
        // 入参；模型、请求信息
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setModel(Model.CHATGLM_LITE); // chatGLM_6b_SSE、chatglm_lite、chatglm_lite_32k、chatglm_std、chatglm_pro
        request.setPrompt(new ArrayList<ChatCompletionRequest.Prompt>() {
            private static final long serialVersionUID = -7988151926241837899L;

            {
                add(ChatCompletionRequest.Prompt.builder()
                        .role(Role.user.getCode())
                        .content("1+1=?")
                        .build());
            }
        });

        CompletableFuture<String> future = openAiSession.completions(request);
        String response = future.get();
        return response;
    }
}
