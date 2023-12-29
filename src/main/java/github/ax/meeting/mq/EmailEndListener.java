package github.ax.meeting.mq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
@RocketMQMessageListener(topic = "chx-mq-end", consumerGroup = "chx-groupB")
public class EmailEndListener implements RocketMQListener<EmailSendEvent> {

    @Resource
    JavaMailSender sender;

    @Value("${spring.mail.username}")
    String username;

    @Override
    public void onMessage(EmailSendEvent emailSendEvent) {
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

    /**
     * 快速封装简单邮件消息实体
     * @param title 标题
     * @param content 内容
     * @param email 收件人
     * @return 邮件实体
     */
    private SimpleMailMessage createMessage(String title, String content, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(title);
        message.setText(content);
        message.setTo(email);
        message.setFrom(username);
        return message;
    }
}
