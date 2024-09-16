package suftware.tuitui.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import suftware.tuitui.common.http.Message;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
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
        List<FollowDto> followerList = followService.getFollowerList(id);
        List<FollowDto> followingList = followService.getFollowingList(id);

        //  팔로워 및 팔로잉 하는 유저가 없음
        if (followerList == null && followingList == null){
            return ResponseEntity.status(TuiTuiMsgCode.FOLLOWS_NOT_FOUND.getHttpStatus()).body(Message.builder()
                    .status(TuiTuiMsgCode.FOLLOWS_NOT_FOUND.getHttpStatus())
                    .code(TuiTuiMsgCode.FOLLOWS_NOT_FOUND.getCode())
                    .message(TuiTuiMsgCode.FOLLOWS_NOT_FOUND.getMsg())
                    .build());
        }
        else {
            FollowResponseDto followResponseDtoList = FollowResponseDto.toDto(followerList, followingList);

            return ResponseEntity.status(TuiTuiMsgCode.FOLLOWS_READ_SUCCESS.getHttpStatus()).body(Message.builder()
                    .status(TuiTuiMsgCode.FOLLOWS_READ_SUCCESS.getHttpStatus())
                    .message(TuiTuiMsgCode.FOLLOWS_READ_SUCCESS.getMsg())
                    .data(followResponseDtoList)
                    .build());
        }
    }

    @PostMapping(value = "profiles/follows")
    public ResponseEntity<Message> createFollow(@RequestBody FollowRequestDto followRequestDto){
        followService.saveFollow(followRequestDto);

        return ResponseEntity.status(TuiTuiMsgCode.FOLLOWS_CREATE_SUCCESS.getHttpStatus()).body(Message.builder()
                .status(TuiTuiMsgCode.FOLLOWS_CREATE_SUCCESS.getHttpStatus())
                .message(TuiTuiMsgCode.FOLLOWS_CREATE_SUCCESS.getMsg())
                .build());
    }

    @DeleteMapping(value = "profiles/follows")
    public ResponseEntity<Message> deleteFollow(@RequestBody FollowRequestDto followRequestDto){
        followService.deleteFollow(followRequestDto);

        return ResponseEntity.status(TuiTuiMsgCode.FOLLOWS_DELETE_SUCCESS.getHttpStatus()).body(Message.builder()
                .status(TuiTuiMsgCode.FOLLOWS_DELETE_SUCCESS.getHttpStatus())
                .message(TuiTuiMsgCode.FOLLOWS_DELETE_SUCCESS.getMsg())
                .build());
    }
}
