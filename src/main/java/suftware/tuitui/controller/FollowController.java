package suftware.tuitui.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import suftware.tuitui.common.http.Message;
import suftware.tuitui.common.enumType.MsgCode;
import suftware.tuitui.dto.request.FollowRequestDto;
import suftware.tuitui.dto.response.FollowDto;
import suftware.tuitui.dto.response.FollowResponseDto;
import suftware.tuitui.service.FollowService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/")
public class FollowController {
    private final FollowService followService;

    @GetMapping(value = "profiles/follows/{profileId}")
    public ResponseEntity<Message> readFollows(@PathVariable(name = "profileId") Integer id){
        Message message = new Message();
        List<FollowDto> followerList = followService.getFollowerList(id);
        List<FollowDto> followingList = followService.getFollowingList(id);

        //  팔로워 및 팔로잉 하는 유저가 없음
        if (followerList == null && followingList == null){
            return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                    .status(HttpStatus.OK)
                    .message(MsgCode.FOLLOWS_NOT_FOUND.getMsg())
                    .build());
        }
        else {
            FollowResponseDto followResponseDtoList = FollowResponseDto.toDto(followerList, followingList);

            return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                    .status(HttpStatus.OK)
                    .message(MsgCode.FOLLOWS_READ_SUCCESS.getMsg())
                    .data(followResponseDtoList)
                    .build());
        }
    }

    @PostMapping(value = "profiles/follows")
    public ResponseEntity<Message> createFollow(FollowRequestDto followRequestDto){

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping(value = "profiles/follows")
    public ResponseEntity<Message> deleteFollow(){

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
