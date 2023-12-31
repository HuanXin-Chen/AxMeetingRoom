package github.ax.meeting.controller;

import github.ax.meeting.annotation.LoginCharacter;
import github.ax.meeting.entities.Msg;
import github.ax.meeting.entities.ShowStatus;
import github.ax.meeting.service.RoomService;
import github.ax.meeting.service.ShowStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// 会议室Api接口
@RestController
public class RoomController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private ShowStatusService showStatusService;


    //根据ID查会议室

    @GetMapping("/room")
    public Msg getRoom(@RequestParam Map<String,Object> para){
        return roomService.getRoom(para);
    }
    //分页查询全部会议室（currentPage,pageSize）
    @LoginCharacter
    @GetMapping("/rooms")
    public Msg getAllRooms(@RequestParam Map<String,Object> para){
        return roomService.getAll(para);
    }
    //修改会议室信息：带什么改什么
    @LoginCharacter
    @PutMapping("/room")
    public Msg update(@RequestBody Map<String,Object> para){
        if(para.containsKey("air")||para.containsKey("projector")){
            return roomService.updateEquipment(para);
        }
        if (para.containsKey("roomStatus")){
            return roomService.updateRoom(para);
        }
        Msg msg = roomService.duplicateCheck(para);
        if(msg.getCode()==100){
            Integer id = (Integer)para.get("roomId");
            Integer size = (Integer)para.get("roomSize");
            ShowStatus showStatus = new ShowStatus(id,size);
            showStatusService.updateMessage(showStatus);
            return  roomService.updateRoom(para);
        }else{
            return msg;
        }
    }
    //添加会议室  （roomNo,roomSize）
    @LoginCharacter
    @PostMapping("/room")
    public Msg add(@RequestBody Map<String,Object> para){
        Msg msg = roomService.duplicateCheck(para);
        if (msg.getCode()==200)
            return msg;
        return roomService.addRoom(para);
    }
    //通过ID删除会议室  --实现连带删除
    @LoginCharacter
    @DeleteMapping("/room")
    public Msg delete(@RequestBody Map<String,Object> para){
        return roomService.deleteRoom(para);
    }
    //分页模糊查询不带id （currentPage,pageSize,....）
    @LoginCharacter
    @GetMapping("/roomNo")
    public Msg selectByNo(@RequestParam Map<String,Object> para){
        return roomService.selectByNo(para);
    }


}
