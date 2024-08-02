package suftware.tuitui.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import suftware.tuitui.config.http.Message;
import suftware.tuitui.config.http.MsgCode;
import suftware.tuitui.dto.request.CommentRequestDto;
import suftware.tuitui.dto.response.CommentResponseDto;
import suftware.tuitui.service.CommentService;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("api/")
public class CommentController {
    private final CommentService commentService;

    //  캡슐 id에 해당하는 모든 댓글 조회
    @GetMapping(value = "capsules/{capsuleId}/comments")
    public ResponseEntity<Message> getCapsuleComment(@PathVariable(name = "capsuleId") Integer id){
        Message message = new Message();
        List<CommentResponseDto> commentResponseDtoList = commentService.getCapsuleComment(id);

        //  댓글이 없는 경우
        if (commentResponseDtoList.isEmpty()){
            message.setStatus(HttpStatus.NOT_FOUND.value());
            message.setMessage(MsgCode.COMMENT_NOT_FOUND.getMsg());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(message);
        }
        else {
            message.setStatus(HttpStatus.OK.value());
            message.setMessage(MsgCode.READ_SUCCESS.getMsg());
            message.setData(commentResponseDtoList);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(message);
        }
    }

    //  전체 댓글 조회
    @GetMapping(value = "capsules/comments")
    public ResponseEntity<Message> getCommentAll(){
        Message message = new Message();
        List<CommentResponseDto> commentResponseDtoList = commentService.getCommentList();

        if (commentResponseDtoList.isEmpty()){
            message.setStatus(HttpStatus.NOT_FOUND.value());
            message.setMessage(MsgCode.COMMENT_NOT_FOUND.getMsg());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(message);
        }
        else {
            message.setStatus(HttpStatus.OK.value());
            message.setMessage(MsgCode.READ_SUCCESS.getMsg());
            message.setData(commentResponseDtoList);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(message);
        }
    }

    //  //  시간에 맞는 댓글 조회
    //  @GetMapping(value = "capsules/comments/")
    //  public ResponseEntity<Message> getCapsuleCommentBy(@RequestBody CommentRequestDto commentRequestDto){
    //      Message message = new Message();
    //
    //  }

    //  댓글 저장
    @PostMapping(value = "capsules/comments")
    public ResponseEntity<Message> addCapsuleComment(@RequestBody CommentRequestDto commentRequestDto){
        Message message = new Message();
        Optional<CommentResponseDto> commentResponseDto = commentService.saveCapsuleComment(commentRequestDto);

        if (commentResponseDto.isEmpty()){
            message.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            message.setMessage(MsgCode.CREATE_FAIL.getMsg());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(message);
        }
        else {
            message.setStatus(HttpStatus.CREATED.value());
            message.setMessage(MsgCode.CREATE_SUCCESS.getMsg());
            message.setData(commentResponseDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(message);
        }
    }

    //  댓글 수정
    @PutMapping(value = "capsules/comments/{commentId}")
    public ResponseEntity<Message> updateCapsuleComment(@PathVariable(name = "commentId") Integer commentId, @RequestBody CommentRequestDto commentRequestDto){
        Message message = new Message();
        Optional<CommentResponseDto> commentResponseDto = commentService.updateCapsuleComment(commentId, commentRequestDto);

        if (commentResponseDto.isEmpty()){
            message.setStatus(HttpStatus.NOT_FOUND.value());
            message.setMessage("commentId: " + commentId + "," + MsgCode.COMMENT_NOT_FOUND.getMsg());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(message);
        }
        else {
            message.setStatus(HttpStatus.OK.value());
            message.setMessage(MsgCode.UPDATE_SUCCESS.getMsg());
            message.setData(commentResponseDto);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(message);
        }
    }

    //  댓글 삭제
    @DeleteMapping(value = "capsules/comments/{commentId}")
    public ResponseEntity<Message> deleteCapsuleComment(@PathVariable(name = "commentId") Integer commentId){
        Message message = commentService.deleteCapsuleComment(commentId);

        return ResponseEntity.status(message.getStatus())
                .body(message);
    }
}
