package suftware.tuitui.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import suftware.tuitui.common.enumType.S3ImagePath;
import suftware.tuitui.common.exception.TuiTuiException;
import suftware.tuitui.common.http.HttpResponse;
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
@Slf4j
public class TimeCapsuleController {
    private final TimeCapsuleService timeCapsuleService;
    private final TimeCapsuleImageService timeCapsuleImageService;

    //  전체 캡슐 조회
    @GetMapping(value = "capsules")
    public ResponseEntity<HttpResponse> readCapsuleList(@RequestParam(name = "pageNo", defaultValue = "0", required = false) Integer pageNo,
                                                        @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                        @RequestParam(name = "sortBy", defaultValue = "writeAt", required = false) String sortBy) {
        Optional<PageResponse> timeCapsulePageResponse = timeCapsuleService.getCapsuleList(pageNo, pageSize, sortBy);

        return HttpResponse.toResponseEntity(TuiTuiMsgCode.CAPSULE_READ_SUCCESS, timeCapsulePageResponse);
    }

    //  캡슐 id 기준 타임 캡슐 조회
    @GetMapping(value = "capsules/{capsuleId}")
    public ResponseEntity<HttpResponse> readCapsule(@PathVariable(name = "capsuleId") Integer timeCapsuleId) {
        Optional<TimeCapsuleResponseDto> timeCapsuleResponseDto = timeCapsuleService.getCapsule(timeCapsuleId);

        return HttpResponse.toResponseEntity(TuiTuiMsgCode.CAPSULE_READ_SUCCESS, timeCapsuleResponseDto);
    }

    //  프로필 id 기준 타임 캡슐 조회
    @GetMapping(value = "profiles/{profileId}/capsules")
    public ResponseEntity<HttpResponse> readCapsuleByWriteUser(@PathVariable(name = "profileId") Integer writeUserId,
                                                               @RequestParam(name = "pageNo", defaultValue = "0", required = false) Integer pageNo,
                                                               @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                               @RequestParam(name = "sortBy", defaultValue = "writeAt", required = false) String sortBy) {
        Optional<PageResponse> timeCapsulePageResponse = timeCapsuleService.getCapsuleByWriteUser(writeUserId, pageNo, pageSize, sortBy);

        return HttpResponse.toResponseEntity(TuiTuiMsgCode.CAPSULE_READ_SUCCESS, timeCapsulePageResponse);
    }

    //  닉네임 기준 타임 캡슐 조회
    @GetMapping(value = "profiles/nicknames/{nickname}/capsules")
    public ResponseEntity<HttpResponse> readCapsuleByNickname(@PathVariable(name = "nickname") String nickname,
                                                              @RequestParam(name = "pageNo", defaultValue = "0", required = false) Integer pageNo,
                                                              @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                              @RequestParam(name = "sortBy", defaultValue = "writeAt", required = false) String sortBy){
        Optional<PageResponse> timeCapsulePageResponse = timeCapsuleService.getCapsuleByNickname(nickname, pageNo, pageSize, sortBy);

        return HttpResponse.toResponseEntity(TuiTuiMsgCode.CAPSULE_READ_SUCCESS, timeCapsulePageResponse);
    }

    //  구면 코사인 법칙 이용 현재 좌표를 받아 거리이내의 캡슐 조회
    @GetMapping(value = "capsules/nearby")
    public ResponseEntity<HttpResponse> readCapsuleByPosition(@RequestParam(name = "latitude") BigDecimal latitude,
                                                              @RequestParam(name = "longitude") BigDecimal longitude,
                                                              @RequestParam(name = "radius", defaultValue = "2", required = false) Double radius,
                                                              @RequestParam(name = "pageNo", defaultValue = "0", required = false) Integer pageNo,
                                                              @RequestParam(name = "pageSize", defaultValue = "10", required = false) Integer pageSize,
                                                              @RequestParam(name = "sortBy", defaultValue = "writeAt", required = false) String sortBy){
        Optional<PageResponse> timeCapsulePageResponse = timeCapsuleService.getCapsuleByPosition(latitude, longitude, radius, pageNo, pageSize, sortBy);

        return HttpResponse.toResponseEntity(TuiTuiMsgCode.CAPSULE_READ_SUCCESS, timeCapsulePageResponse);
    }

    // 캡슐 저장 이미지 포함
    @PostMapping(value = "capsules/with-image", consumes = "multipart/form-data")
    public ResponseEntity<HttpResponse> createCapsuleWithImages(@RequestPart(name = "request") TimeCapsuleRequestDto timeCapsuleRequestDto,
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

            log.info("Create Capsule Id: {}", timeCapsuleResponseDto.getCapsuleId());

            return HttpResponse.toResponseEntity(TuiTuiMsgCode.CAPSULE_CREATE_SUCCESS, timeCapsuleResponseDto);
        } else {
            log.info("Create Capsule Fail");
            return HttpResponse.toResponseEntity(TuiTuiMsgCode.CAPSULE_CREATE_FAIL);
        }
    }

    //  캡슐 저장
    @PostMapping(value = "capsules/without-image")
    public ResponseEntity<HttpResponse> createCapsuleWithJson(@RequestBody TimeCapsuleRequestDto timeCapsuleRequestDto) throws JsonProcessingException {
        Optional<TimeCapsuleResponseDto> timeCapsuleResponseDto = timeCapsuleService.save(timeCapsuleRequestDto);

        log.info("Create Capsule Id: {}", timeCapsuleResponseDto.get().getCapsuleId());

        return HttpResponse.toResponseEntity(TuiTuiMsgCode.CAPSULE_CREATE_SUCCESS, timeCapsuleResponseDto);
    }

    //  캡슐 수정
    @PutMapping(value = "capsules")
    public ResponseEntity<HttpResponse> updateCapsule(@RequestBody TimeCapsuleRequestDto timeCapsuleRequestDto) {
        Optional<TimeCapsuleResponseDto> timeCapsuleResponseDto = timeCapsuleService.updateCapsule(timeCapsuleRequestDto.getCapsuleId(),
                timeCapsuleRequestDto);

        return HttpResponse.toResponseEntity(TuiTuiMsgCode.CAPSULE_UPDATE_SUCCESS, timeCapsuleResponseDto);
    }

    //  캡슐 삭제
    @DeleteMapping(value = "capsules/{capsuleId}")
    public ResponseEntity<HttpResponse> deleteCapsule(@PathVariable(name = "capsuleId") Integer id){
        timeCapsuleImageService.deleteCapsuleImage(id);
        timeCapsuleService.deleteCapsule(id);

        return HttpResponse.toResponseEntity(TuiTuiMsgCode.CAPSULE_DELETE_SUCCESS);
    }
}