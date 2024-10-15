package suftware.tuitui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suftware.tuitui.common.exception.TuiTuiException;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.common.time.DateTimeUtil;
import suftware.tuitui.domain.Comment;
import suftware.tuitui.domain.Profile;
import suftware.tuitui.domain.TimeCapsule;
import suftware.tuitui.dto.request.CommentRequestDto;
import suftware.tuitui.dto.response.CommentResponseDto;
import suftware.tuitui.repository.CommentRepository;
import suftware.tuitui.repository.ProfileRepository;
import suftware.tuitui.repository.TimeCapsuleRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ProfileRepository profileRepository;
    private final TimeCapsuleRepository timeCapsuleRepository;

    //  전체 댓글 리스트 조회
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentList(){
        List<Comment> commentList =  commentRepository.findAll();

        if (commentList.isEmpty()){
            throw new TuiTuiException(TuiTuiMsgCode.COMMENT_NOT_FOUND);
        }

        List<CommentResponseDto> commentRequestDtoList = new ArrayList<>();
        for (Comment comment : commentList){
            commentRequestDtoList.add(CommentResponseDto.toDTO(comment));
        }

        return commentRequestDtoList;
    }

    //  캡슐에 해당하는 모든 댓글 조회
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCapsuleComment(Integer id){
        List<Comment> commentList =  commentRepository.findByTimeCapsule_TimeCapsuleId(id, Sort.by(Sort.Order.asc("updateAt")));

        //  캡슐에 댓글이 없음
        if (commentList.isEmpty()){
            throw new TuiTuiException(TuiTuiMsgCode.COMMENT_NOT_FOUND);
        }

        List<CommentResponseDto> commentRequestDtoList = new ArrayList<>();
        for (Comment comment : commentList){
            commentRequestDtoList.add(CommentResponseDto.toDTO(comment));
        }

        return commentRequestDtoList;
    }

    //  댓글 저장
    public Optional<CommentResponseDto> saveCapsuleComment(CommentRequestDto commentRequestDto) {
        Profile profile = profileRepository.findById(commentRequestDto.getProfileId())
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.PROFILE_NOT_FOUND));

        TimeCapsule timeCapsule = timeCapsuleRepository.findById(commentRequestDto.getTimeCapsuleId())
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.CAPSULE_NOT_FOUND));

        Comment comment;

        if (!(commentRequestDto.getParentCommentId() == null)) {
            Comment childComment = commentRepository.findById(commentRequestDto.getParentCommentId())
                    .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.COMMENT_NOT_FOUND));

            comment = commentRepository.save(CommentRequestDto.toEntity(commentRequestDto, childComment, profile, timeCapsule));
        } else {
            comment = commentRepository.save(CommentRequestDto.toEntity(commentRequestDto, profile, timeCapsule));
        }

        return Optional.of(CommentResponseDto.toDTO(comment));
    }

    //  댓글 수정
    @Transactional
    public Optional<CommentResponseDto> updateCapsuleComment(CommentRequestDto commentRequestDto) {
        Comment comment = commentRepository.findById(commentRequestDto.getCommentId())
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.COMMENT_NOT_FOUND));

        comment.updateComment(commentRequestDto.getComment());

        return Optional.of(CommentResponseDto.toDTO(comment));
    }

    //  댓글 삭제
    @Transactional
    public void deleteCapsuleComment(Integer id) {
        if (!commentRepository.existsById(id)) {
            throw new TuiTuiException(TuiTuiMsgCode.COMMENT_NOT_FOUND);
        }

        commentRepository.deleteById(id);
    }
}
