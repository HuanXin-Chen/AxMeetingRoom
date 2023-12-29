package github.ax.meeting.mq;

import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Date;
import java.util.Map;


public class EmailSendEvent extends BaseEvent<Map<String,Object>>{

    public static EmailSendEvent create(Map<String,Object> message) {
        EmailSendEvent event = new EmailSendEvent();
        event.setId(RandomStringUtils.randomNumeric(11));
        event.setTimestamp(new Date());
        event.setData(message);
        return event;
    }
}
