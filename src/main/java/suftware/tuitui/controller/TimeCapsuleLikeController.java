package suftware.tuitui.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import suftware.tuitui.common.http.HttpResponse;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.dto.request.TimeCapsuleLikeRequestDto;
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
    public ResponseEntity<HttpResponse> readCapsuleLike(@PathVariable(name = "capsuleId") Integer capsule_id) {
        List<TimeCapsuleLikeResponseDto> timeCapsuleLikeResponseDtoList = timeCapsuleLikeService.getCapsuleLike(capsule_id);

        return HttpResponse.toResponseEntity(TuiTuiMsgCode.CAPSULE_LIKE_READ_SUCCESS, timeCapsuleLikeResponseDtoList);
    }

    //  캡슐 좋아요 저장
    @PostMapping(value = "capsules/likes")
    public ResponseEntity<HttpResponse> createCapsuleLike(@RequestBody TimeCapsuleLikeRequestDto timeCapsuleLikeRequestDto) {
        Optional<TimeCapsuleLikeResponseDto> timeCapsuleLikeResponseDto = timeCapsuleLikeService.saveCapsuleLike(timeCapsuleLikeRequestDto);

        return HttpResponse.toResponseEntity(TuiTuiMsgCode.CAPSULE_LIKE_CREATE_SUCCESS, timeCapsuleLikeResponseDto);
    }

    //  캡슐 좋아요 삭제
    @DeleteMapping(value = "capsules/likes/{capsuleLikeId}")
    public ResponseEntity<HttpResponse> deleteCapsuleLike(@PathVariable(name = "capsuleLikeId") Integer timeCapsuleLikeId){
        timeCapsuleLikeService.deleteCapsuleLike(timeCapsuleLikeId);

        return HttpResponse.toResponseEntity(TuiTuiMsgCode.CAPSULE_LIKE_DELETE_SUCCESS);
    }
}
