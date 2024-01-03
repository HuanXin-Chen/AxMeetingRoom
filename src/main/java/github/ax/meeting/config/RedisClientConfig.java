package github.ax.meeting.config;

import github.ax.meeting.annotation.RedisTopic;
import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.redisson.config.Config;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// redis客户端配置

@Configuration
@EnableConfigurationProperties(RedisClientConfigProperties.class)
public class RedisClientConfig {

    /**
     * 参考官方文档quick start可知配置方式
     */
    @Bean("redissonClient")
    public RedissonClient redissonClient(ConfigurableApplicationContext applicationContext, RedisClientConfigProperties properties) {
        Config config = new Config();

        config.useSingleServer()
                .setAddress("redis://" + properties.getHost() + ":" + properties.getPort())
                .setPassword(properties.getPassword())
                .setConnectionPoolSize(properties.getPoolSize())
                .setConnectionMinimumIdleSize(properties.getMinIdleSize())
                .setIdleConnectionTimeout(properties.getIdleTimeout())
                .setConnectTimeout(properties.getConnectTimeout())
                .setRetryAttempts(properties.getRetryAttempts())
                .setRetryInterval(properties.getRetryInterval())
                .setPingConnectionInterval(properties.getPingInterval())
                .setKeepAlive(properties.isKeepAlive()); // lombok对于Boolean生成的是is类型判断

        //System.out.println("redis连接池配置成功");

        RedissonClient redissonClient = Redisson.create(config);

//        //配置发布订阅
//        String[] beanNamesForType = applicationContext.getBeanNamesForType(MessageListener.class);
//        for(String beanName : beanNamesForType) {
//            MessageListener bean = applicationContext.getBean(beanName, MessageListener.class);
//
//            Class<? extends MessageListener> beanClass = bean.getClass();
//
//            if(beanClass.isAnnotationPresent(RedisTopic.class)) {
//                RedisTopic redisTopic = beanClass.getAnnotation(RedisTopic.class);
//                RTopic topic = redissonClient.getTopic(redisTopic.topic());
//                topic.addListener(String.class,bean);
//
//                // 动态创建 bean 对象，注入到 spring 容器，bean 的名称为 redisTopic，对象为 RTopic
//                ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
//                beanFactory.registerSingleton(redisTopic.topic(), topic);
//            }
//        }

        return redissonClient;
    }
}
