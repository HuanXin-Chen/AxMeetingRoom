package github.ax.meeting.util;

import github.ax.meeting.entities.EmailSendEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description:
 * @author：H_X
 * @date: 2024-01-15
 * @Copyright： 公众号：阿新的杂记
 */
@Component
public class EmailUtils {

    @Resource
    JavaMailSender sender;

    @Value("${spring.mail.username}")
    String username;


    private SimpleMailMessage createMessage(String title, String content, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(title);
        message.setText(content);
        message.setTo(email);
        message.setFrom(username);
        return message;
    }

    public void sendInitEmail(EmailSendEvent emailSendEvent) {
        String email = emailSendEvent.getData().get("email").toString();
        String room = emailSendEvent.getData().get("room_no").toString();
        String time = emailSendEvent.getData().get("time").toString();
        SimpleMailMessage message = createMessage("预约成功通知",
                "你预约的会议室" + room + ",时间为" + time + "，预计三天内进行审核，请留意结果！",
                email);

        if(message == null) return;
        sender.send(message);
    }

    public void sendEndEmail(EmailSendEvent emailSendEvent) {
        String email = emailSendEvent.getData().get("email").toString();
        String room = emailSendEvent.getData().get("room_no").toString();
        String time = emailSendEvent.getData().get("time").toString();
        String reason = (String) emailSendEvent.getData().get("reason");
        String status = (String) emailSendEvent.getData().get("status");
        System.out.println(status);
        SimpleMailMessage message = null;
        if(status.equals("1")) {
            message = createMessage("预约审核通知",
                    "你预约的会议室" + room + ",时间为" + time + "，审核成功，祝你使用愉快！",
                    email);
        }
        else {
            message = createMessage("预约审核通知",
                    "你预约的会议室" + room + ",时间为" + time + "，审核失败，原因为：" + reason + "。",
                    email);
        }

        if(message == null) return;
        sender.send(message);
    }

}
