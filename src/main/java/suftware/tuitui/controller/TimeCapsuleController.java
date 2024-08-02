package suftware.tuitui.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import suftware.tuitui.config.http.Message;
import suftware.tuitui.config.http.MsgCode;
import suftware.tuitui.domain.TimeCapsule;
import suftware.tuitui.dto.request.TimeCapsuleRequestDto;
import suftware.tuitui.dto.response.ProfileResponseDto;
import suftware.tuitui.dto.response.TimeCapsuleResponseDto;
import suftware.tuitui.dto.response.TimeCapsuleVisitResponseDto;
import suftware.tuitui.service.TimeCapsuleLikeService;
import suftware.tuitui.service.TimeCapsuleService;
import suftware.tuitui.service.TimeCapsuleVisitService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("api/")
public class TimeCapsuleController {
    private TimeCapsuleService timeCapsuleService;

    //  전체 캡슐 조회
    @GetMapping(value = "capsules")
    public ResponseEntity<Message> getCapsuleList(){
        Message message = new Message();
        List<TimeCapsuleResponseDto> timeCapsuleResponseDtoList = timeCapsuleService.getCapsuleList();

        if (timeCapsuleResponseDtoList.isEmpty()){
            message.setStatus(HttpStatus.NOT_FOUND.value());
            message.setMessage(MsgCode.CAPSULE_NOT_FOUND.getMsg());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(message);
        }
        else{
            message.setStatus(HttpStatus.OK.value());
            message.setMessage(MsgCode.READ_SUCCESS.getMsg());
            message.setData(timeCapsuleResponseDtoList);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(message);
        }
    }

    //  캡슐 id 기준 타임 캡슐 조회
    @GetMapping(value = "capsules/{capsuleId}")
    public ResponseEntity<Message> getCapsule(@PathVariable(name = "capsuleId") Integer timeCapsuleId){
        Message message = new Message();
        Optional<TimeCapsuleResponseDto> timeCapsuleResponseDto = timeCapsuleService.getCapsule(timeCapsuleId);

        if (timeCapsuleResponseDto.isEmpty()){
            message.setStatus(HttpStatus.NOT_FOUND.value());
            message.setMessage(MsgCode.CAPSULE_NOT_FOUND.getMsg());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(message);
        }
        else{
            message.setStatus(HttpStatus.OK.value());
            message.setMessage(MsgCode.READ_SUCCESS.getMsg());
            message.setData(timeCapsuleResponseDto);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(message);
        }
    }

    //  프로필 id 기준 타임 캡슐 조회
    @GetMapping(value = "profiles/{profileId}/capsules")
    public ResponseEntity<Message> getCapsuleByWriteUser(@PathVariable(name = "profileId") Integer writeUserId){
        Message message = new Message();
        List<TimeCapsuleResponseDto> timeCapsuleResponseDtoList = timeCapsuleService.getCapsuleByWriteUser(writeUserId);

        if (timeCapsuleResponseDtoList.isEmpty()){
            message.setStatus(HttpStatus.NOT_FOUND.value());
            message.setMessage(MsgCode.CAPSULE_NOT_FOUND.getMsg());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(message);
        }
        else{
            message.setStatus(HttpStatus.OK.value());
            message.setMessage(MsgCode.READ_SUCCESS.getMsg());
            message.setData(timeCapsuleResponseDtoList);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(message);
        }
    }

    //  닉네임 기준 타임 캡슐 조회
    @GetMapping(value = "profiles/nicknames/{nickname}/capsules")
    public ResponseEntity<Message> getCapsuleByNickname(@PathVariable(name = "nickname") String nickname){
        Message message = new Message();
        List<TimeCapsuleResponseDto> timeCapsuleResponseDtoList = timeCapsuleService.getCapsuleByNickname(nickname);

        if (timeCapsuleResponseDtoList.isEmpty()){
            message.setStatus(HttpStatus.NOT_FOUND.value());
            message.setMessage(MsgCode.CAPSULE_NOT_FOUND.getMsg());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(message);
        }
        else{
            message.setStatus(HttpStatus.OK.value());
            message.setMessage(MsgCode.READ_SUCCESS.getMsg());
            message.setData(timeCapsuleResponseDtoList);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(message);
        }
    }

    //  캡슐 저장
    @PostMapping(value = "capsules/without-image")
    public ResponseEntity<Message> saveCapsule(@RequestBody TimeCapsuleRequestDto timeCapsuleRequestDto) {
        Message message = new Message();
        Optional<TimeCapsuleResponseDto> timeCapsuleResponseDto = timeCapsuleService.save(timeCapsuleRequestDto);

        if (timeCapsuleResponseDto.isEmpty()){
            message.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            message.setMessage(MsgCode.CAPSULE_CREATE_FAIL.getMsg());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(message);
        }
        else{
            message.setStatus(HttpStatus.CREATED.value());
            message.setMessage(MsgCode.CAPSULE_CREATE_SUCCESS.getMsg());
            message.setData(timeCapsuleResponseDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(message);
        }
    }

    //  캡슐 저장, 이미지 포함, 수정 필요.
    @PostMapping(value = "capsules/with-image", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Message> saveCapsule(@RequestPart(name = "request") TimeCapsuleRequestDto timeCapsuleRequestDto,
                                               @RequestPart(name = "file", required = false ) List<MultipartFile> files) throws IOException {
        Message message = new Message();

        //  2024.07.22
        //  여기서부터 save 메서드 수정 필요.
        Optional<TimeCapsuleResponseDto> timeCapsuleResponseDto = timeCapsuleService.save(timeCapsuleRequestDto);

        if (timeCapsuleResponseDto.isEmpty()){
            message.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            message.setMessage(MsgCode.CAPSULE_CREATE_FAIL.getMsg());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(message);
        }
        else {
            message.setStatus(HttpStatus.CREATED.value());
            message.setMessage(MsgCode.CAPSULE_CREATE_SUCCESS.getMsg());
            message.setData(timeCapsuleResponseDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(message);
        }
    }

    //  캡슐 수정
    @PutMapping(value = "capsules/{capsuleId}")
    public ResponseEntity<Message> updateCapsule(@PathVariable(name = "capsuleId") Integer id, @RequestBody TimeCapsuleRequestDto timeCapsuleRequestDto){
        Message message = new Message();
        Optional<TimeCapsuleResponseDto> timeCapsuleResponseDto = timeCapsuleService.updateCapsule(id, timeCapsuleRequestDto);

        if (timeCapsuleResponseDto.isEmpty()){
            message.setStatus(HttpStatus.NOT_FOUND.value());
            message.setMessage(MsgCode.CAPSULE_NOT_FOUND.getMsg());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(message);
        }
        else {
            message.setStatus(HttpStatus.OK.value());
            message.setMessage(MsgCode.UPDATE_SUCCESS.getMsg());
            message.setData(timeCapsuleResponseDto);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(message);
        }
    }

    //  캡슐 삭제
    @DeleteMapping(value = "capsules/{capsuleId}")
    public ResponseEntity<Message> deleteCapsule(@PathVariable(name = "capsuleId") Integer id){
        Message message = timeCapsuleService.deleteCapsule(id);

        return ResponseEntity.status(message.getStatus())
                .body(message);
    }


    //  임시 테스트용 api
    //  추후 파라미터값으로 받은 값만큼 타임캡슐 반환하는 api 로 작성 예정
    @GetMapping(value = "capsules/test")
    public ResponseEntity<Message> capsuleTest(){
        Message message = new Message();
        List<TimeCapsuleResponseDto> timeCapsuleResponseDtoList = new ArrayList<>();

        for (int i = 20; i <= 40; i++) {
            Optional<TimeCapsuleResponseDto> timeCapsuleResponseDto = timeCapsuleService.getCapsule(i);
            timeCapsuleResponseDto.ifPresent(timeCapsuleResponseDtoList::add);
        }

        if (timeCapsuleResponseDtoList.isEmpty()){
            message.setStatus(HttpStatus.NOT_FOUND.value());
            message.setMessage(MsgCode.CAPSULE_NOT_FOUND.getMsg());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(message);
        }
        else{
            message.setStatus(HttpStatus.OK.value());
            message.setMessage(MsgCode.READ_SUCCESS.getMsg());
            message.setData(timeCapsuleResponseDtoList);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(message);
        }
    }
}
