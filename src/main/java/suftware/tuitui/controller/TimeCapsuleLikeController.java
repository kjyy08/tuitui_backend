package suftware.tuitui.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import suftware.tuitui.config.http.Message;
import suftware.tuitui.config.http.MsgCode;
import suftware.tuitui.dto.request.TimeCapsuleLikeRequestDto;
import suftware.tuitui.dto.response.ProfileResponseDto;
import suftware.tuitui.dto.response.TimeCapsuleLikeResponseDto;
import suftware.tuitui.service.TimeCapsuleLikeService;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("api/")
public class TimeCapsuleLikeController {
    private final TimeCapsuleLikeService timeCapsuleLikeService;

    //  해당하는 캡슐 좋아요를 누른 모든 유저 조회
    @GetMapping(value = "capsules/{capsuleId}/likes")
    public ResponseEntity<Message> getCapsuleLike(@PathVariable(name = "capsuleId") Integer capsule_id){
        Message message = new Message();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        List<ProfileResponseDto> profileResponseDtoList = timeCapsuleLikeService.getCapsuleLike(capsule_id);

        //  좋아요를 누른 유저가 존재하지 않음
        if (profileResponseDtoList.isEmpty()){
            message.setStatus(HttpStatus.NOT_FOUND.value());
            message.setMessage(MsgCode.CAPSULE_LIKE_EMPTY.getMsg());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .headers(headers)
                    .body(message);
        }

        //  좋아요를 누른 유저가 1명 이상 존재
        else {
            message.setStatus(HttpStatus.OK.value());
            message.setMessage(MsgCode.READ_SUCCESS.getMsg());
            message.setData(profileResponseDtoList);
            return ResponseEntity.status(HttpStatus.OK)
                    .headers(headers)
                    .body(message);
        }
    }

    //  캡슐 좋아요 저장
    @PostMapping(value = "capsules/likes")
    public ResponseEntity<Message> saveCapsuleLike(@RequestBody TimeCapsuleLikeRequestDto timeCapsuleLikeRequestDto){
        Message message = new Message();
        Optional<TimeCapsuleLikeResponseDto> timeCapsuleLikeResponseDto = timeCapsuleLikeService.saveCapsuleLike(timeCapsuleLikeRequestDto);

        if (timeCapsuleLikeResponseDto.isEmpty()){
            message.setStatus(HttpStatus.CONFLICT.value());
            message.setMessage(MsgCode.CREATE_FAIL.getMsg());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(message);
        }
        else {
            message.setStatus(HttpStatus.CREATED.value());
            message.setMessage(MsgCode.CREATE_SUCCESS.getMsg());
            message.setData(timeCapsuleLikeResponseDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(message);
        }
    }

    //  캡슐 좋아요 삭제
    @DeleteMapping(value = "capsules/likes/{capsuleLikeId}")
    public ResponseEntity<Message> deleteCapsuleLike(@PathVariable(name = "capsuleLikeId") Integer timeCapsuleLikeId){
        Message message = timeCapsuleLikeService.deleteCapsuleLike(timeCapsuleLikeId);

        return ResponseEntity.status(message.getStatus())
                .body(message);
    }
}
