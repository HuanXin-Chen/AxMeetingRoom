package github.ax.meeting.config;

import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Maybatis配置

@Configuration
public class MybatisConfig {

    @Bean
    public ConfigurationCustomizer getCustomizer(){
        return configuration -> configuration.setMapUnderscoreToCamelCase(true);
    }
}