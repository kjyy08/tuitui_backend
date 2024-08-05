package suftware.tuitui.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import suftware.tuitui.common.http.Message;
import suftware.tuitui.common.enumType.MsgCode;
import suftware.tuitui.dto.request.TimeCapsuleLikeRequestDto;
import suftware.tuitui.dto.response.ProfileResponseDto;
import suftware.tuitui.dto.response.TimeCapsuleLikeResponseDto;
import suftware.tuitui.service.TimeCapsuleLikeService;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("api/")
public class TimeCapsuleLikeController {
    private final TimeCapsuleLikeService timeCapsuleLikeService;

    //  해당하는 캡슐 좋아요를 누른 모든 유저 조회
    @GetMapping(value = "capsules/{capsuleId}/likes")
    public ResponseEntity<Message> readCapsuleLike(@PathVariable(name = "capsuleId") Integer capsule_id) {
        List<ProfileResponseDto> profileResponseDtoList = timeCapsuleLikeService.getCapsuleLike(capsule_id);

        if (profileResponseDtoList.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                    .status(HttpStatus.OK)
                    .message(MsgCode.CAPSULE_LIKE_NOT_FOUND.getMsg())
                    .build());
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                    .status(HttpStatus.OK)
                    .message(MsgCode.CAPSULE_LIKE_READ_SUCCESS.getMsg())
                    .data(profileResponseDtoList)
                    .build());
        }
    }

    //  캡슐 좋아요 저장
    @PostMapping(value = "capsules/likes")
    public ResponseEntity<Message> createCapsuleLike(@RequestBody TimeCapsuleLikeRequestDto timeCapsuleLikeRequestDto) {
        Optional<TimeCapsuleLikeResponseDto> timeCapsuleLikeResponseDto = timeCapsuleLikeService.saveCapsuleLike(timeCapsuleLikeRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(Message.builder()
                .status(HttpStatus.CREATED)
                .message(MsgCode.CAPSULE_LIKE_CREATE_SUCCESS.getMsg())
                .data(timeCapsuleLikeResponseDto)
                .build());
    }

    //  캡슐 좋아요 삭제
    @DeleteMapping(value = "capsules/likes/{capsuleLikeId}")
    public ResponseEntity<Message> deleteCapsuleLike(@PathVariable(name = "capsuleLikeId") Integer timeCapsuleLikeId){
        timeCapsuleLikeService.deleteCapsuleLike(timeCapsuleLikeId);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(MsgCode.CAPSULE_LIKE_DELETE_SUCCESS.getMsg())
                .build());
    }
}
