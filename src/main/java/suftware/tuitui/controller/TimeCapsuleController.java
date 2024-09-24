package suftware.tuitui.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import suftware.tuitui.common.enumType.S3ImagePath;
import suftware.tuitui.common.exception.TuiTuiException;
import suftware.tuitui.common.http.Message;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.dto.request.TimeCapsuleRequestDto;
import suftware.tuitui.dto.response.ImageResponseDto;
import suftware.tuitui.dto.response.PageResponse;
import suftware.tuitui.dto.response.TimeCapsuleResponseDto;
import suftware.tuitui.service.TimeCapsuleImageService;
import suftware.tuitui.service.TimeCapsuleService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("api/")
public class TimeCapsuleController {
    private final TimeCapsuleService timeCapsuleService;
    private final TimeCapsuleImageService timeCapsuleImageService;

    //  전체 캡슐 조회
    @GetMapping(value = "capsules")
    public ResponseEntity<Message> readCapsuleList(@RequestParam(name = "pageNo", defaultValue = "0", required = false) Integer pageNo,
                                                   @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                   @RequestParam(name = "sortBy", defaultValue = "writeAt", required = false) String sortBy) {
        Optional<PageResponse> timeCapsulePageResponse = timeCapsuleService.getCapsuleList(pageNo, pageSize, sortBy);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(TuiTuiMsgCode.CAPSULE_READ_SUCCESS.getMsg())
                .data(timeCapsulePageResponse)
                .build());
    }

    //  캡슐 id 기준 타임 캡슐 조회
    @GetMapping(value = "capsules/{capsuleId}")
    public ResponseEntity<Message> readCapsule(@PathVariable(name = "capsuleId") Integer timeCapsuleId) {
        Optional<TimeCapsuleResponseDto> timeCapsuleResponseDto = timeCapsuleService.getCapsule(timeCapsuleId);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(TuiTuiMsgCode.CAPSULE_READ_SUCCESS.getMsg())
                .data(timeCapsuleResponseDto)
                .build());
    }

    //  프로필 id 기준 타임 캡슐 조회
    @GetMapping(value = "profiles/{profileId}/capsules")
    public ResponseEntity<Message> readCapsuleByWriteUser(@PathVariable(name = "profileId") Integer writeUserId,
                                                          @RequestParam(name = "pageNo", defaultValue = "0", required = false) Integer pageNo,
                                                          @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                          @RequestParam(name = "sortBy", defaultValue = "writeAt", required = false) String sortBy) {
        Optional<PageResponse> timeCapsulePageResponse = timeCapsuleService.getCapsuleByWriteUser(writeUserId, pageNo, pageSize, sortBy);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(TuiTuiMsgCode.CAPSULE_READ_SUCCESS.getMsg())
                .data(timeCapsulePageResponse)
                .build());
    }

    //  닉네임 기준 타임 캡슐 조회
    @GetMapping(value = "profiles/nicknames/{nickname}/capsules")
    public ResponseEntity<Message> readCapsuleByNickname(@PathVariable(name = "nickname") String nickname,
                                                         @RequestParam(name = "pageNo", defaultValue = "0", required = false) Integer pageNo,
                                                         @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                         @RequestParam(name = "sortBy", defaultValue = "writeAt", required = false) String sortBy){
        Optional<PageResponse> timeCapsulePageResponse = timeCapsuleService.getCapsuleByNickname(nickname, pageNo, pageSize, sortBy);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(TuiTuiMsgCode.CAPSULE_READ_SUCCESS.getMsg())
                .data(timeCapsulePageResponse)
                .build());
    }

    //  구면 코사인 법칙 이용 현재 좌표를 받아 거리이내의 캡슐 조회
    @GetMapping(value = "capsules/nearby")
    public ResponseEntity<Message> readCapsuleByPosition(@RequestParam(name = "latitude") BigDecimal latitude,
                                                         @RequestParam(name = "longitude") BigDecimal longitude,
                                                         @RequestParam(name = "radius", defaultValue = "2", required = false) Double radius,
                                                         @RequestParam(name = "pageNo", defaultValue = "0", required = false) Integer pageNo,
                                                         @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                         @RequestParam(name = "sortBy", defaultValue = "writeAt", required = false) String sortBy){
        Optional<PageResponse> timeCapsulePageResponse = timeCapsuleService.getCapsuleByPosition(latitude, longitude, radius, pageNo, pageSize, sortBy);

        return ResponseEntity.status(TuiTuiMsgCode.CAPSULE_READ_SUCCESS.getHttpStatus()).body(Message.builder()
                .status(TuiTuiMsgCode.CAPSULE_READ_SUCCESS.getHttpStatus())
                .message(TuiTuiMsgCode.CAPSULE_READ_SUCCESS.getMsg())
                .data(timeCapsulePageResponse)
                .build());
    }

    // 캡슐 저장 이미지 포함
    @PostMapping(value = "capsules/with-image", consumes = "multipart/form-data")
    public ResponseEntity<Message> createCapsuleWithImages(@RequestPart(name = "request") TimeCapsuleRequestDto timeCapsuleRequestDto,
                                                           @RequestPart(name = "file") List<MultipartFile> files) throws IOException{
        //  이미지가 포함된 경우에만 create 수행
        if(files != null && !files.isEmpty()) {
            // TimeCapsule 저장
            TimeCapsuleResponseDto timeCapsuleResponseDto = timeCapsuleService.save(timeCapsuleRequestDto)
                    .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.CAPSULE_CREATE_FAIL));

            // Image 저장
            List<ImageResponseDto> imageResponseDtoList = new ArrayList<>();

            for (MultipartFile file : files) {
                imageResponseDtoList.add(timeCapsuleImageService.uploadCapsuleImage(timeCapsuleResponseDto.getCapsuleId(), S3ImagePath.CAPSULE.getPath(), file)
                        .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.IMAGE_S3_UPLOAD_FAIL)));
            }

            timeCapsuleResponseDto.setImageList(imageResponseDtoList);

            return ResponseEntity.status(TuiTuiMsgCode.CAPSULE_CREATE_SUCCESS.getHttpStatus()).body(Message.builder()
                    .status(TuiTuiMsgCode.CAPSULE_CREATE_SUCCESS.getHttpStatus())
                    .message(TuiTuiMsgCode.CAPSULE_CREATE_SUCCESS.getMsg())
                    .data(timeCapsuleResponseDto)
                    .build());
        } else {
            return ResponseEntity.status(TuiTuiMsgCode.CAPSULE_CREATE_FAIL.getHttpStatus()).body(Message.builder()
                    .status(TuiTuiMsgCode.CAPSULE_CREATE_FAIL.getHttpStatus())
                    .message(TuiTuiMsgCode.CAPSULE_CREATE_FAIL.getMsg())
                    .code(TuiTuiMsgCode.CAPSULE_CREATE_FAIL.getCode())
                    .build());
        }
    }

    //  캡슐 저장
    @PostMapping(value = "capsules/without-image")
    public ResponseEntity<Message> createCapsuleWithJson(@RequestBody TimeCapsuleRequestDto timeCapsuleRequestDto) {
        Optional<TimeCapsuleResponseDto> timeCapsuleResponseDto = timeCapsuleService.save(timeCapsuleRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(Message.builder()
                .status(HttpStatus.CREATED)
                .message(TuiTuiMsgCode.CAPSULE_CREATE_SUCCESS.getMsg())
                .data(timeCapsuleResponseDto)
                .build());
    }

    //  캡슐 수정
    @PutMapping(value = "capsules")
    public ResponseEntity<Message> updateCapsule(@RequestBody TimeCapsuleRequestDto timeCapsuleRequestDto) {
        Optional<TimeCapsuleResponseDto> timeCapsuleResponseDto = timeCapsuleService.updateCapsule(timeCapsuleRequestDto.getCapsuleId(),
                timeCapsuleRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(TuiTuiMsgCode.CAPSULE_UPDATE_SUCCESS.getMsg())
                .data(timeCapsuleResponseDto)
                .build());
    }

    //  캡슐 삭제
    @DeleteMapping(value = "capsules/{capsuleId}")
    public ResponseEntity<Message> deleteCapsule(@PathVariable(name = "capsuleId") Integer id){
        timeCapsuleImageService.deleteCapsuleImage(id);
        timeCapsuleService.deleteCapsule(id);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(TuiTuiMsgCode.CAPSULE_DELETE_SUCCESS.getMsg())
                .build());
    }
}