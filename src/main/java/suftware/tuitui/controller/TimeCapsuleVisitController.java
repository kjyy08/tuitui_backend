package suftware.tuitui.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import suftware.tuitui.common.http.HttpResponse;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.dto.response.TimeCapsuleVisitResponseDto;
import suftware.tuitui.service.TimeCapsuleVisitService;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("api/")
public class TimeCapsuleVisitController {
    private TimeCapsuleVisitService timeCapsuleVisitService;

    //  캡슐을 방문한 모든 유저 조회
    @GetMapping(value = "capsules/{capsuleId}/visits")
    public ResponseEntity<HttpResponse> readCapsuleVisitCount(@PathVariable(name = "capsuleId") Integer capsuleId) {
        Optional<TimeCapsuleVisitResponseDto> timeCapsuleVisitResponseDto = timeCapsuleVisitService.getCapsuleVisitCount(capsuleId);

        return HttpResponse.toResponseEntity(TuiTuiMsgCode.CAPSULE_VISIT_READ_SUCCESS, timeCapsuleVisitResponseDto);
    }

    //  조회수 증가
    //  쿠키, 세션 등의 방법이 있으나 조회수 증가만을 목적으로 두기에
    //  api로 주고 받도록 구현
    @PostMapping(value = "capsules/{capsuleId}/visits")
    public ResponseEntity<HttpResponse> createCapsuleVisitCount(@PathVariable(name = "capsuleId") Integer capsuleId) {
        Optional<TimeCapsuleVisitResponseDto> timeCapsuleVisitResponseDto = timeCapsuleVisitService.addCapsuleVisitCount(capsuleId);

        return HttpResponse.toResponseEntity(TuiTuiMsgCode.CAPSULE_VISIT_CREATE_SUCCESS, timeCapsuleVisitResponseDto);
    }
}
