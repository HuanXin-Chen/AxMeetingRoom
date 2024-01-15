package github.ax.meeting.entities;

import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Date;
import java.util.Map;

// 消息类
@Data
public class EmailSendEvent {

    private String id;
    private Date timestamp;
    private Map<String,Object> data;

    public static EmailSendEvent create(Map<String,Object> message) {
        EmailSendEvent event = new EmailSendEvent();
        event.setId(RandomStringUtils.randomNumeric(11));
        event.setTimestamp(new Date());
        event.setData(message);
        return event;
    }
}
