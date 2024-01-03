package github.ax.meeting.config;

import github.ax.meeting.aop.RateLimiterAOP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 限流配置
@Slf4j
@Configuration
public class RateLimiterAOPConfig {
    @Bean
    public RateLimiterAOP rateLimiterAOP() {
        return new RateLimiterAOP();
    }
}
