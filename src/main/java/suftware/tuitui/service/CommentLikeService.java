package suftware.tuitui.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suftware.tuitui.common.exception.CustomException;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.domain.Comment;
import suftware.tuitui.domain.Profile;
import suftware.tuitui.domain.CommentLike;
import suftware.tuitui.dto.request.CommentLikeRequestDto;
import suftware.tuitui.dto.response.CommentLikeResponseDto;
import suftware.tuitui.repository.CommentRepository;
import suftware.tuitui.repository.ProfileRepository;
import suftware.tuitui.repository.CommentLikeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CommentLikeService {
    private final ProfileRepository profileRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    // 좋아요 누른 유저 목록
    public List<CommentLikeResponseDto> getCommentLike(Integer commentId){
        List<CommentLike> commentLikes = commentLikeRepository.findByCommentId(commentId);

        if(commentLikes.isEmpty()){
            throw new CustomException(TuiTuiMsgCode.COMMENT_LIKE_NOT_FOUND);
        }
        List<CommentLikeResponseDto> likeResponseDtos = new ArrayList<>();
        for(CommentLike like: commentLikes){
            likeResponseDtos.add(CommentLikeResponseDto.toDto(like));
        }
        return likeResponseDtos;
    }

    // 좋아요 추가
    public Optional<CommentLikeResponseDto> saveCapsuleLike(CommentLikeRequestDto commentLikeRequestDto){
        Profile profile = profileRepository.findById(commentLikeRequestDto.getProfileId())
                .orElseThrow(() -> new CustomException(TuiTuiMsgCode.PROFILE_NOT_FOUND));
        Comment comment = commentRepository.findById(commentLikeRequestDto.getCommentId())
                .orElseThrow(() -> new CustomException(TuiTuiMsgCode.COMMENT_NOT_FOUND));

        if(commentLikeRepository.existsByComment_CommentIdAndProfile_ProfileId(comment.getCommentId(), profile.getProfileId())){
            throw new CustomException(TuiTuiMsgCode.COMMENT_LIKE_EXIST);
        }

        CommentLike commentLike = commentLikeRepository.save(CommentLikeRequestDto.toEntity(comment, profile));

        return Optional.of(CommentLikeResponseDto.toDto(commentLike));
    }

    // 좋아요 제거
    @Transactional
    public void deleteCommentLike(CommentLikeRequestDto commentLikeRequestDto) {
        Integer commentId = commentLikeRequestDto.getCommentId();
        Integer profileId = commentLikeRequestDto.getProfileId();

        CommentLike commentLike = commentLikeRepository.findByComment_CommentIdAndProfile_ProfileId(commentId, profileId)
                .orElseThrow(() -> new CustomException(TuiTuiMsgCode.COMMENT_LIKE_NOT_FOUND));

        commentLikeRepository.delete(commentLike);
    }

}
