package github.ax.meeting.mq;

import com.alibaba.fastjson.JSON;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author：H_X
 * @date: 2023-12-29
 * @Copyright： 公众号：阿新的杂记
 */
@Component
@Slf4j
public class EventPublisher {

    @Setter(onMethod_ = @Autowired)
    private RocketMQTemplate rocketmqTemplate;

    /**
     * 普通消息
     *
     * @param topic   主题
     * @param message 消息
     */
    public void publish(String topic, EmailSendEvent message) {
        try {
            log.info("发送MQ消息 topic:{} message:{}", topic, JSON.toJSONString(message));
            rocketmqTemplate.convertAndSend(topic, message);
        } catch (Exception e) {
            log.error("发送MQ消息失败 topic:{} message:{}", topic, JSON.toJSONString(message), e);
            // 大部分MQ发送失败后，会需要任务补偿
        }
    }

    /**
     * 延迟消息
     *
     * @param topic          主题
     * @param message        消息
     * @param delayTimeLevel 延迟时长
     */
    public void publishDelivery(String topic, BaseEvent<?> message, int delayTimeLevel) {
        try {
            String mqMessage = JSON.toJSONString(message);
            log.info("发送MQ延迟消息 topic:{} message:{}", topic, mqMessage);
            rocketmqTemplate.syncSend(topic, MessageBuilder.withPayload(message).build(), 1000, delayTimeLevel);
        } catch (Exception e) {
            log.error("发送MQ延迟消息失败 topic:{} message:{}", topic, JSON.toJSONString(message), e);
            // 大部分MQ发送失败后，会需要任务补偿
        }
    }

}
