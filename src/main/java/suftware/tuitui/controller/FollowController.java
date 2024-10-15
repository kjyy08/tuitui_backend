package suftware.tuitui.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import suftware.tuitui.common.http.HttpResponseDto;
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
    public ResponseEntity<HttpResponseDto> readFollows(@PathVariable(name = "profileId") Integer id){
        List<FollowDto> followerList = followService.getFollowerList(id);
        List<FollowDto> followingList = followService.getFollowingList(id);

        //  팔로워 및 팔로잉 하는 유저가 없음
        if (followerList == null && followingList == null){
            return HttpResponseDto.toResponseEntity(TuiTuiMsgCode.FOLLOWS_NOT_FOUND);
        }
        else {
            FollowResponseDto followResponseDtoList = FollowResponseDto.toDto(followerList, followingList);

            return HttpResponseDto.toResponseEntity(TuiTuiMsgCode.FOLLOWS_READ_SUCCESS, followResponseDtoList);
        }
    }

    @PostMapping(value = "profiles/follows")
    public ResponseEntity<HttpResponseDto> createFollow(@RequestBody FollowRequestDto followRequestDto){
        followService.saveFollow(followRequestDto);

        return HttpResponseDto.toResponseEntity(TuiTuiMsgCode.FOLLOWS_CREATE_SUCCESS);
    }

    @DeleteMapping(value = "profiles/follows")
    public ResponseEntity<HttpResponseDto> deleteFollow(@RequestBody FollowRequestDto followRequestDto){
        followService.deleteFollow(followRequestDto);

        return HttpResponseDto.toResponseEntity(TuiTuiMsgCode.FOLLOWS_DELETE_SUCCESS);
    }
}
