package github.ax.meeting;

import com.alibaba.fastjson.JSON;
import github.ax.meeting.entities.MeetingRecord;
import github.ax.meeting.mapper.MeetingRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author：H_X
 * @date: 2023-12-29
 * @Copyright： 公众号：阿新的杂记
 */
@Slf4j
@SpringBootTest
public class DaoTest {
    @Autowired
    MeetingRecordMapper meetingRecordMapper;

    @Test
    public void test(){
        long l = System.currentTimeMillis();
        Date meetingDate =new Date(l);
        log.info("当前时间：{}", meetingDate);
        System.out.println(meetingDate);
        MeetingRecord meetingRecord = new MeetingRecord();
        meetingRecord.setMeetingDate(meetingDate);
        List<MeetingRecord> meetingRecords = meetingRecordMapper.selectByConditionFree(meetingDate);
        log.info("查询结果：{}", meetingRecords.toString());
    }
}
