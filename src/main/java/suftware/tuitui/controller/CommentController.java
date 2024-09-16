package suftware.tuitui.controller;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import suftware.tuitui.common.exception.TuiTuiException;
import suftware.tuitui.common.http.Message;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.dto.request.CommentRequestDto;
import suftware.tuitui.dto.response.CommentResponseDto;
import suftware.tuitui.service.CommentService;


import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("api/")
public class CommentController {
    private final CommentService commentService;

    //  캡슐 id에 해당하는 모든 댓글 조회
    @GetMapping(value = "capsules/{capsuleId}/comments")
    public ResponseEntity<Message> readCapsuleComment(@PathVariable(name = "capsuleId") Integer id) {
        List<CommentResponseDto> commentResponseDtoList = commentService.getCapsuleComment(id);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(TuiTuiMsgCode.COMMENT_READ_SUCCESS.getMsg())
                .data(commentResponseDtoList)
                .build());
    }

    //  전체 댓글 조회
    @GetMapping(value = "capsules/comments")
    public ResponseEntity<Message> readCommentAll() {
        List<CommentResponseDto> commentResponseDtoList = commentService.getCommentList();

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(TuiTuiMsgCode.COMMENT_READ_SUCCESS.getMsg())
                .data(commentResponseDtoList)
                .build());
    }

    //  댓글 저장
    @PostMapping(value = "capsules/comments")
    public ResponseEntity<Message> createCapsuleComment(@Valid @RequestBody CommentRequestDto commentRequestDto) {
        Optional<CommentResponseDto> commentResponseDto = commentService.saveCapsuleComment(commentRequestDto);

        return ResponseEntity.status(TuiTuiMsgCode.COMMENT_CREATE_SUCCESS.getHttpStatus()).body(Message.builder()
                .status(TuiTuiMsgCode.COMMENT_CREATE_SUCCESS.getHttpStatus())
                .message(TuiTuiMsgCode.COMMENT_CREATE_SUCCESS.getMsg())
                .data(commentResponseDto)
                .build());
    }

    //  댓글 수정
    @PutMapping(value = "capsules/comments")
    public ResponseEntity<Message> updateCapsuleComment(@Valid @RequestBody CommentRequestDto commentRequestDto){
        Optional<CommentResponseDto> commentResponseDto = commentService.updateCapsuleComment(commentRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(TuiTuiMsgCode.COMMENT_UPDATE_SUCCESS.getMsg())
                .data(commentResponseDto)
                .build());
    }

    //  댓글 삭제
    @DeleteMapping(value = "capsules/comments/{commentId}")
    public ResponseEntity<Message> deleteCapsuleComment(@PathVariable(name = "commentId") Integer commentId){
        commentService.deleteCapsuleComment(commentId);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(TuiTuiMsgCode.COMMENT_DELETE_SUCCESS.getMsg())
                .build());
    }

}
