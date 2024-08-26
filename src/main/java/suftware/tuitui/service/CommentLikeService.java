package suftware.tuitui.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suftware.tuitui.common.exception.CustomException;
import suftware.tuitui.common.enumType.MsgCode;
import suftware.tuitui.domain.Comment;
import suftware.tuitui.domain.Profile;
import suftware.tuitui.domain.CommentLike;
import suftware.tuitui.dto.request.CommentLikeRequestDto;
import suftware.tuitui.dto.request.CommentRequestDto;
import suftware.tuitui.dto.response.CommentLikeResponseDto;
import suftware.tuitui.dto.response.CommentResponseDto;
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

    // 좋아요 추가
    @Transactional
    public void saveCapsuleLike(CommentLikeRequestDto commentLikeRequestDto){
        Profile profile = profileRepository.findById(commentLikeRequestDto.getProfileId())
                .orElseThrow(() -> new CustomException(MsgCode.PROFILE_NOT_FOUND));
        Comment comment = commentRepository.findById(commentLikeRequestDto.getCommentId())
                .orElseThrow(() -> new CustomException(MsgCode.COMMENT_NOT_FOUND));

        if(commentLikeRepository.existsByComment_CommentIdAndProfile_ProfileId(comment.getCommentId(), profile.getProfileId())){
            throw new CustomException(MsgCode.COMMENT_LIKE_EXIST);
        }
        CommentLike commentLike = CommentLike.builder()
                .comment(comment)
                .profile(profile)
                .build();

        commentLikeRepository.save(commentLike);
    }

    // 좋아요 제거
    @Transactional
    public void removeCapsuleLike(CommentLikeRequestDto commentLikeRequestDto) {
        Integer commentId = commentLikeRequestDto.getCommentId();
        Integer profileId = commentLikeRequestDto.getProfileId();

        if (!commentLikeRepository.existsByComment_CommentIdAndProfile_ProfileId(commentId, profileId)){
            throw new CustomException(MsgCode.COMMENT_LIKE_NOT_FOUND);
        }

        commentLikeRepository.deleteByComment_CommentIdAndProfile_ProfileId(commentId, profileId);
    }

    // 좋아요 누른 유저 목록
    public List<CommentLikeResponseDto> getCommentLike(Integer commentId){
        List<CommentLike> commentLikes = commentLikeRepository.findByComment_CommentId(commentId);

        if(commentLikes.isEmpty()){
            throw new CustomException(MsgCode.COMMENT_LIKE_NOT_FOUND);
        }
        List<CommentLikeResponseDto> likeResponseDtos = new ArrayList<>();
        for(CommentLike like: commentLikes){
            likeResponseDtos.add(CommentLikeResponseDto.toDto(like));
        }
        return likeResponseDtos;
    }
}
