package suftware.tuitui.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import suftware.tuitui.config.http.Message;
import suftware.tuitui.config.http.MsgCode;
import suftware.tuitui.dto.response.ProfileResponseDto;
import suftware.tuitui.dto.response.TimeCapsuleLikeResponseDto;
import suftware.tuitui.dto.response.TimeCapsuleVisitResponseDto;
import suftware.tuitui.service.TimeCapsuleVisitService;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("api/")
public class TimeCapsuleVisitController {
    private TimeCapsuleVisitService timeCapsuleVisitService;

    //  캡슐을 방문한 모든 유저 조회
    @GetMapping(value = "capsules/{capsuleId}/visits")
    public ResponseEntity<Message> getCapsuleVisitCount(@PathVariable(name = "capsuleId") Integer capsuleId){
        Message message = new Message();
        Optional<TimeCapsuleVisitResponseDto> timeCapsuleVisitResponseDto = timeCapsuleVisitService.getCapsuleVisitCount(capsuleId);

        if (timeCapsuleVisitResponseDto.isEmpty()){
            message.setStatus(HttpStatus.NOT_FOUND.value());
            message.setMessage(MsgCode.READ_FAIL.getMsg());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(message);
        }
        else {
            message.setStatus(HttpStatus.OK.value());
            message.setMessage(MsgCode.READ_SUCCESS.getMsg());
            message.setData(timeCapsuleVisitResponseDto);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(message);
        }
    }

    //  조회수 증가
    //  쿠키, 세션 등의 방법이 있으나 조회수 증가만을 목적으로 두기에
    //  간단하게 구현
    @PostMapping(value = "capsules/{capsuleId}/visits")
    public ResponseEntity<Message> addCapsuleVisitCount(@PathVariable(name = "capsuleId") Integer capsuleId){
        Message message = new Message();
        Optional<TimeCapsuleVisitResponseDto> timeCapsuleVisitResponseDto = timeCapsuleVisitService.addCapsuleVisitCount(capsuleId);

        if (timeCapsuleVisitResponseDto.isEmpty()){
            message.setStatus(HttpStatus.NOT_FOUND.value());
            message.setMessage("capsuleId: " + capsuleId + ", " + MsgCode.CAPSULE_NOT_FOUND.getMsg());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(message);
        }
        else {
            message.setStatus(HttpStatus.OK.value());
            message.setMessage(MsgCode.CREATE_SUCCESS.getMsg());
            message.setData(timeCapsuleVisitResponseDto);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(message);
        }
    }
}
