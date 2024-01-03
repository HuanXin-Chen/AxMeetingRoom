package github.ax.meeting.service;

import github.ax.meeting.entities.MeetingRecord;
import github.ax.meeting.entities.Room;
import github.ax.meeting.mapper.MeetingRecordMapper;
import github.ax.meeting.mapper.RoomMapper;
import github.huanxin.chatgpt.model.ChatCompletionRequest;
import github.huanxin.chatgpt.model.Model;
import github.huanxin.chatgpt.model.Role;
import github.huanxin.chatgpt.session.OpenAiSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

// Ai基础服务


@Slf4j
@Service
public class AiChatService {

    @Autowired(required = false)
    private OpenAiSession openAiSession;

    @Autowired
    private MeetingRecordMapper meetingRecordMapper;

    @Autowired
    private RoomMapper roomMapper;


    @Async
    public CompletableFuture<String> completions( ) throws InterruptedException, ExecutionException {

        List<MeetingRecord> meetingRecords = meetingRecordMapper.selectByConditionFree(new Date(System.currentTimeMillis()));
        List<Room>  rooms = roomMapper.selectAll();
        for(MeetingRecord meetingRecord : meetingRecords) {
            for(Room room : rooms) {
                if(meetingRecord.getRoomId().equals(room.getRoomId())) {
                    meetingRecord.setRoomId(null);
                    meetingRecord.setRoomNo(room.getRoomNo());
                    break;
                }
            }
        }

        String head = "我把你对接到了我的会议室系统中，你需要给我提供一些建议。会议室可预约时间段为5个，分别为9：00-10：30，10：30-12：00，13：00-15：00，15：00-17：00，17：00-19：00。" +
                "我现在要预约一间空闲会议室，不知道预约哪个时间段，请你给我一些预约建议，告诉我哪个时间段比较好即可,字数控制在20字内。";

//        String room_info = "这是房间信息，注意room_no才是真正的房间编号，room_id是数据库的主键，不能告诉外人，你不要回答错了！！！" + rooms.toString();
        String record = "最近五天的会议被占用的情况如下，其中json串meeting_slot的数字表示该时间段不可以预约，meeting_data为被占用时间,room_no为该时间段被占用的会议室编号。" + meetingRecords.toString();

        // 入参；模型、请求信息
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setModel(Model.CHATGLM_LITE); // chatGLM_6b_SSE、chatglm_lite、chatglm_lite_32k、chatglm_std、chatglm_pro
        request.setPrompt(new ArrayList<ChatCompletionRequest.Prompt>() {
            private static final long serialVersionUID = -7988151926241837899L;

            {
                add(ChatCompletionRequest.Prompt.builder()
                        .role(Role.user.getCode())
                        .content(head +  record)
                        .build());
            }
        });

        CompletableFuture<String> future = openAiSession.completions(request);
        return future;
    }
}
