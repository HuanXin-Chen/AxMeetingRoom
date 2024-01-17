package github.ax.meeting;

import github.ax.meeting.entities.EmailSendEvent;
import github.ax.meeting.util.EmailUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author：H_X
 * @date: 2024-01-17
 * @Copyright： 公众号：阿新的杂记
 */
@SpringBootTest
public class EmailTest {

    @Resource
    EmailUtils emailUtils;

    @Test
    public void sendEmail( ) {
        Map<String,Object> map = new HashMap<>();
        map.put("email","1056216208@qq.com");
        map.put("room_no","1001");
        map.put("time","2023-01-17 10:00:00");

        emailUtils.sendInitEmail(EmailSendEvent.create(map));
    }
}
