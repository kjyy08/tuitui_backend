package suftware.tuitui.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import suftware.tuitui.common.http.Message;
import suftware.tuitui.common.enumType.MsgCode;
import suftware.tuitui.dto.request.CommentRequestDto;
import suftware.tuitui.dto.response.CommentResponseDto;
import suftware.tuitui.service.CommentService;
import suftware.tuitui.service.CommentLikeService;
import suftware.tuitui.dto.request.CommentLikeRequestDto;
import suftware.tuitui.dto.response.CommentLikeResponseDto;



import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("api/")
public class CommentController {
    private final CommentService commentService;
    private final CommentLikeService commentLikeService;

    //  캡슐 id에 해당하는 모든 댓글 조회
    @GetMapping(value = "capsules/{capsuleId}/comments")
    public ResponseEntity<Message> readCapsuleComment(@PathVariable(name = "capsuleId") Integer id) {
        List<CommentResponseDto> commentResponseDtoList = commentService.getCapsuleComment(id);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(MsgCode.COMMENT_READ_SUCCESS.getMsg())
                .data(commentResponseDtoList)
                .build());
    }

    //  전체 댓글 조회
    @GetMapping(value = "capsules/comments")
    public ResponseEntity<Message> readCommentAll() {
        List<CommentResponseDto> commentResponseDtoList = commentService.getCommentList();

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(MsgCode.COMMENT_READ_SUCCESS.getMsg())
                .data(commentResponseDtoList)
                .build());
    }

    //  댓글 저장
    @PostMapping(value = "capsules/comments")
    public ResponseEntity<Message> createCapsuleComment(@RequestBody CommentRequestDto commentRequestDto) {
        Optional<CommentResponseDto> commentResponseDto = commentService.saveCapsuleComment(commentRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(MsgCode.COMMENT_CREATE_SUCCESS.getMsg())
                .data(commentResponseDto)
                .build());
    }

    //  댓글 수정
    @PutMapping(value = "capsules/comments/{commentId}")
    public ResponseEntity<Message> updateCapsuleComment(@PathVariable(name = "commentId") Integer commentId, @RequestBody CommentRequestDto commentRequestDto){
        Optional<CommentResponseDto> commentResponseDto = commentService.updateCapsuleComment(commentId, commentRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(MsgCode.COMMENT_UPDATE_SUCCESS.getMsg())
                .data(commentResponseDto)
                .build());
    }

    //  댓글 삭제
    @DeleteMapping(value = "capsules/comments/{commentId}")
    public ResponseEntity<Message> deleteCapsuleComment(@PathVariable(name = "commentId") Integer commentId){
        commentService.deleteCapsuleComment(commentId);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(MsgCode.COMMENT_DELETE_SUCCESS.getMsg())
                .build());
    }

    // 댓글 좋아요 추가
    @PostMapping(value = "comments/likes")
    public ResponseEntity<Message> addCommentLike(@RequestBody CommentLikeRequestDto commentLikeRequestDto){
        commentLikeService.saveCapsuleLike(commentLikeRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(Message.builder()
                .status(HttpStatus.CREATED)
                .message(MsgCode.COMMENT_LIKE_CREATE_SUCCESS.getMsg())
                .build());
    }

    // 댓글 좋아요 삭제
    @DeleteMapping(value = "comments/likes")
    public ResponseEntity<Message> deleteCommentLike(@RequestBody CommentLikeRequestDto commentLikeRequestDto){
        commentLikeService.removeCapsuleLike(commentLikeRequestDto);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Message.builder()
                .status(HttpStatus.NO_CONTENT)
                .message(MsgCode.COMMENT_LIKE_DELETE_SUCCESS.getMsg())
                .build());
    }
    
    // 댓글 좋아요 유저 조회
    @GetMapping(value = "comments/likes/{commentId}")
    public ResponseEntity<Message> readCommentLike(@PathVariable(name = "commentId") Integer id){
        List<CommentLikeResponseDto> likes = commentLikeService.getCommentLike(id);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(MsgCode.COMMENT_LIKE_READ_SUCCESS.getMsg())
                .data(likes)
                .build());
    }

}
