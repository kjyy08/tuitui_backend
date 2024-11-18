package suftware.tuitui.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.common.http.HttpResponse;
import suftware.tuitui.dto.request.CommentLikeRequestDto;
import suftware.tuitui.dto.response.CommentLikeResponseDto;
import suftware.tuitui.service.CommentLikeService;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("api/")
@Slf4j
public class CommentLikeController {
    private final CommentLikeService commentLikeService;

    // 댓글 좋아요 유저 조회
    @GetMapping(value = "comments/likes/{commentId}")
    public ResponseEntity<HttpResponse> readCommentLike(@PathVariable(name = "commentId") Integer id){
        List<CommentLikeResponseDto> commentLikeResponseDto = commentLikeService.getCommentLike(id);

        return HttpResponse.toResponseEntity(TuiTuiMsgCode.COMMENT_LIKE_READ_SUCCESS, commentLikeResponseDto);
    }

    // 댓글 좋아요 추가
    @PostMapping(value = "comments/likes")
    public ResponseEntity<HttpResponse> createCommentLike(@RequestBody CommentLikeRequestDto commentLikeRequestDto){
        Optional<CommentLikeResponseDto> commentLikeResponseDto = commentLikeService.saveCapsuleLike(commentLikeRequestDto);

        return HttpResponse.toResponseEntity(TuiTuiMsgCode.COMMENT_LIKE_CREATE_SUCCESS, commentLikeResponseDto);
    }

    // 댓글 좋아요 삭제
    @DeleteMapping(value = "comments/likes")
    public ResponseEntity<HttpResponse> deleteCommentLike(@RequestBody CommentLikeRequestDto commentLikeRequestDto){
        commentLikeService.deleteCommentLike(commentLikeRequestDto);

        return HttpResponse.toResponseEntity(TuiTuiMsgCode.COMMENT_LIKE_DELETE_SUCCESS);
    }
}
