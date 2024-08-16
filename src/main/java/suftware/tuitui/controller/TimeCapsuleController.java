package suftware.tuitui.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import suftware.tuitui.common.exception.CustomException;
import suftware.tuitui.common.http.Message;
import suftware.tuitui.common.enumType.MsgCode;
import suftware.tuitui.dto.request.ImageRequestDto;
import suftware.tuitui.dto.request.TimeCapsuleRequestDto;
import suftware.tuitui.dto.response.ImageResponseDto;
import suftware.tuitui.dto.response.TimeCapsuleResponseDto;
import suftware.tuitui.service.ImageService;
import suftware.tuitui.service.TimeCapsuleService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("api/")
public class TimeCapsuleController {
    private final TimeCapsuleService timeCapsuleService;
    private final ImageService imageService;

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    //  전체 캡슐 조회
    @GetMapping(value = "capsules")
    public ResponseEntity<Message> readCapsuleList() {
        List<TimeCapsuleResponseDto> timeCapsuleResponseDtoList = timeCapsuleService.getCapsuleList();

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(MsgCode.CAPSULE_READ_SUCCESS.getMsg())
                .data(timeCapsuleResponseDtoList)
                .build());
    }

    //  캡슐 id 기준 타임 캡슐 조회
    @GetMapping(value = "capsules/{capsuleId}")
    public ResponseEntity<Message> readCapsule(@PathVariable(name = "capsuleId") Integer timeCapsuleId) {
        Optional<TimeCapsuleResponseDto> timeCapsuleResponseDto = timeCapsuleService.getCapsule(timeCapsuleId);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(MsgCode.CAPSULE_READ_SUCCESS.getMsg())
                .data(timeCapsuleResponseDto)
                .build());
    }

    //  프로필 id 기준 타임 캡슐 조회
    @GetMapping(value = "profiles/{profileId}/capsules")
    public ResponseEntity<Message> readCapsuleByWriteUser(@PathVariable(name = "profileId") Integer writeUserId) {
        List<TimeCapsuleResponseDto> timeCapsuleResponseDtoList = timeCapsuleService.getCapsuleByWriteUser(writeUserId);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(MsgCode.CAPSULE_READ_SUCCESS.getMsg())
                .data(timeCapsuleResponseDtoList)
                .build());
    }

    //  닉네임 기준 타임 캡슐 조회
    @GetMapping(value = "profiles/nicknames/{nickname}/capsules")
    public ResponseEntity<Message> readCapsuleByNickname(@PathVariable(name = "nickname") String nickname){
        List<TimeCapsuleResponseDto> timeCapsuleResponseDtoList = timeCapsuleService.getCapsuleByNickname(nickname);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(MsgCode.CAPSULE_READ_SUCCESS.getMsg())
                .data(timeCapsuleResponseDtoList)
                .build());
    }

    //  캡슐 저장
    @PostMapping(value = "capsules/without-image")
    public ResponseEntity<Message> createCapsuleJson(@RequestBody TimeCapsuleRequestDto timeCapsuleRequestDto) {
        Optional<TimeCapsuleResponseDto> timeCapsuleResponseDto = timeCapsuleService.save(timeCapsuleRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(Message.builder()
                .status(HttpStatus.CREATED)
                .message(MsgCode.CAPSULE_CREATE_SUCCESS.getMsg())
                .data(timeCapsuleResponseDto)
                .build());
    }

    //  캡슐 저장, 이미지 포함, 테스트해보고 괜찮으면 url 수정
    @PostMapping(value = "capsules/with-image", consumes = "multipart/form-data")
    public ResponseEntity<Message> createCapsuleMultipartImage(@RequestPart(name = "request") TimeCapsuleRequestDto timeCapsuleRequestDto,
                                                               @RequestPart(name = "file", required = false ) List<MultipartFile> files) throws IOException {
        // TimeCapsule 저장 부분
        TimeCapsuleResponseDto timeCapsuleResponseDto = timeCapsuleService.save(timeCapsuleRequestDto)
                .orElseThrow(() -> new CustomException(MsgCode.CAPSULE_CREATE_FAIL));

        // imageUpload 부분
        List<ImageResponseDto> imageResponseDtoList = new ArrayList<>();
        if(files != null && !files.isEmpty()){
            for(MultipartFile file: files){
                imageResponseDtoList.add(imageService.uploadImage("image_image", timeCapsuleResponseDto.getCapsuleId(), file)
                        .orElseThrow(() -> new CustomException(MsgCode.IMAGES_CREATE_FAIL)));
            }

            timeCapsuleResponseDto.setImageList(imageResponseDtoList);

            return ResponseEntity.status(HttpStatus.CREATED).body(Message.builder()
                    .status(HttpStatus.CREATED)
                    .message(MsgCode.CAPSULE_CREATE_SUCCESS.getMsg())
                    .data(timeCapsuleResponseDto)
                    .build());
        }
        //  파일이 비어있는 상태
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Message.builder()
                            .status(HttpStatus.BAD_REQUEST)
                            .message(MsgCode.CAPSULE_CREATE_FAIL.getMsg())
                            .build());
        }
    }

    //  캡슐 수정
    @PutMapping(value = "capsules/{capsuleId}")
    public ResponseEntity<Message> updateCapsule(@PathVariable(name = "capsuleId") Integer id, @RequestBody TimeCapsuleRequestDto timeCapsuleRequestDto) {
        Optional<TimeCapsuleResponseDto> timeCapsuleResponseDto = timeCapsuleService.updateCapsule(id, timeCapsuleRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(MsgCode.CAPSULE_UPDATE_SUCCESS.getMsg())
                .data(timeCapsuleResponseDto)
                .build());
    }

    //  캡슐 삭제
    @DeleteMapping(value = "capsules/{capsuleId}")
    public ResponseEntity<Message> deleteCapsule(@PathVariable(name = "capsuleId") Integer id){
        timeCapsuleService.deleteCapsule(id);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(MsgCode.CAPSULE_DELETE_SUCCESS.getMsg())
                .build());
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
            throw new CustomException(MsgCode.CAPSULE_NOT_FOUND);
        }
        else {
            return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                    .status(HttpStatus.OK)
                    .message(MsgCode.CAPSULE_READ_SUCCESS.getMsg())
                    .data(timeCapsuleResponseDtoList)
                    .build());
        }
    }
}