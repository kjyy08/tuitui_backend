package suftware.tuitui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import suftware.tuitui.common.enumType.MsgCode;
import suftware.tuitui.common.http.Message;
import suftware.tuitui.dto.request.CommentLikeRequestDto;
import suftware.tuitui.dto.response.CommentLikeResponseDto;
import suftware.tuitui.service.CommentLikeService;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/")
public class CommentLikeController {
    private final CommentLikeService commentLikeService;

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

    // 댓글 좋아요 추가
    @PostMapping(value = "comments/likes")
    public ResponseEntity<Message> createCommentLike(@RequestBody CommentLikeRequestDto commentLikeRequestDto){
        Optional<CommentLikeResponseDto> commentLikeResponseDto = commentLikeService.saveCapsuleLike(commentLikeRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(Message.builder()
                .status(HttpStatus.CREATED)
                .message(MsgCode.COMMENT_LIKE_CREATE_SUCCESS.getMsg())
                .data(commentLikeResponseDto)
                .build());
    }

    // 댓글 좋아요 삭제
    @DeleteMapping(value = "comments/likes")
    public ResponseEntity<Message> deleteCommentLike(@RequestBody CommentLikeRequestDto commentLikeRequestDto){
        commentLikeService.deleteCommentLike(commentLikeRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(MsgCode.COMMENT_LIKE_DELETE_SUCCESS.getMsg())
                .build());
    }
}
