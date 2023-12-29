package github.ax.meeting;

import github.ax.meeting.mq.BaseEvent;
import github.ax.meeting.mq.EmailSendEvent;
import github.ax.meeting.mq.EventPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @description:
 * @author：H_X
 * @date: 2023-12-29
 * @Copyright： 公众号：阿新的杂记
 */
@SpringBootTest
public class MQTest {

    @Autowired
    private EventPublisher eventPublisher;

    @Test
    public void send( ) throws InterruptedException {
        eventPublisher.publish("chx-mq-init",  EmailSendEvent.create(null));
        Thread.sleep(20000);
    }

}
