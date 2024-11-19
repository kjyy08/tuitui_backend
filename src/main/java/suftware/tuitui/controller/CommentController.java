package suftware.tuitui.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import suftware.tuitui.common.http.HttpResponse;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.dto.request.CommentRequestDto;
import suftware.tuitui.dto.response.CommentResponseDto;
import suftware.tuitui.service.CommentService;


import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("api/")
public class CommentController {
    private static final Logger log = LoggerFactory.getLogger(CommentController.class);
    private final CommentService commentService;

    //  캡슐 id에 해당하는 모든 댓글 조회
    @GetMapping(value = "capsules/{capsuleId}/comments")
    public ResponseEntity<HttpResponse> readCapsuleComment(@PathVariable(name = "capsuleId") Integer id) {
        List<CommentResponseDto> commentResponseDtoList = commentService.getCapsuleComment(id);

        return HttpResponse.toResponseEntity(TuiTuiMsgCode.COMMENT_READ_SUCCESS, commentResponseDtoList);
    }

    //  전체 댓글 조회
    @GetMapping(value = "capsules/comments")
    public ResponseEntity<HttpResponse> readCommentAll() {
        List<CommentResponseDto> commentResponseDtoList = commentService.getCommentList();

        return HttpResponse.toResponseEntity(TuiTuiMsgCode.COMMENT_READ_SUCCESS, commentResponseDtoList);
    }

    //  댓글 저장
    @PostMapping(value = "capsules/comments")
    public ResponseEntity<HttpResponse> createCapsuleComment(@Valid @RequestBody CommentRequestDto commentRequestDto) throws JsonProcessingException {
        Optional<CommentResponseDto> commentResponseDto = commentService.saveCapsuleComment(commentRequestDto);

        log.info("Create Capsule Comment Id: {}", commentResponseDto.get().getCommentId());

        return HttpResponse.toResponseEntity(TuiTuiMsgCode.COMMENT_CREATE_SUCCESS, commentResponseDto);
    }

    //  댓글 수정
    @PutMapping(value = "capsules/comments")
    public ResponseEntity<HttpResponse> updateCapsuleComment(@Valid @RequestBody CommentRequestDto commentRequestDto){
        Optional<CommentResponseDto> commentResponseDto = commentService.updateCapsuleComment(commentRequestDto);

        return HttpResponse.toResponseEntity(TuiTuiMsgCode.COMMENT_UPDATE_SUCCESS, commentResponseDto);
    }

    //  댓글 삭제
    @DeleteMapping(value = "capsules/comments/{commentId}")
    public ResponseEntity<HttpResponse> deleteCapsuleComment(@PathVariable(name = "commentId") Integer commentId){
        commentService.deleteCapsuleComment(commentId);

        return HttpResponse.toResponseEntity(TuiTuiMsgCode.COMMENT_DELETE_SUCCESS);
    }
}
