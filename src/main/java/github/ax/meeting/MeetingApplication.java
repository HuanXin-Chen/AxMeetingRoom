package github.ax.meeting;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@MapperScan("github.ax.meeting.mapper")
@SpringBootApplication
@Configurable
public class MeetingApplication {


    public static void main(String[] args) {
        SpringApplication.run(MeetingApplication.class, args);
    }

}
