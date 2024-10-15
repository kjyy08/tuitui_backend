package suftware.tuitui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.common.http.HttpResponseDto;
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
    public ResponseEntity<HttpResponseDto> readCommentLike(@PathVariable(name = "commentId") Integer id){
        List<CommentLikeResponseDto> commentLikeResponseDto = commentLikeService.getCommentLike(id);

        return HttpResponseDto.toResponseEntity(TuiTuiMsgCode.COMMENT_LIKE_READ_SUCCESS, commentLikeResponseDto);
    }

    // 댓글 좋아요 추가
    @PostMapping(value = "comments/likes")
    public ResponseEntity<HttpResponseDto> createCommentLike(@RequestBody CommentLikeRequestDto commentLikeRequestDto){
        Optional<CommentLikeResponseDto> commentLikeResponseDto = commentLikeService.saveCapsuleLike(commentLikeRequestDto);

        return HttpResponseDto.toResponseEntity(TuiTuiMsgCode.COMMENT_LIKE_CREATE_SUCCESS, commentLikeResponseDto);
    }

    // 댓글 좋아요 삭제
    @DeleteMapping(value = "comments/likes")
    public ResponseEntity<HttpResponseDto> deleteCommentLike(@RequestBody CommentLikeRequestDto commentLikeRequestDto){
        commentLikeService.deleteCommentLike(commentLikeRequestDto);

        return HttpResponseDto.toResponseEntity(TuiTuiMsgCode.COMMENT_LIKE_DELETE_SUCCESS);
    }
}
