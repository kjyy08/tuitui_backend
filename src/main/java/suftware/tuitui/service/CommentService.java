package suftware.tuitui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suftware.tuitui.config.http.Message;
import suftware.tuitui.config.http.MsgCode;
import suftware.tuitui.domain.Comment;
import suftware.tuitui.domain.Profile;
import suftware.tuitui.domain.TimeCapsule;
import suftware.tuitui.dto.request.CommentRequestDto;
import suftware.tuitui.dto.response.CommentResponseDto;
import suftware.tuitui.repository.CommentRepository;
import suftware.tuitui.repository.ProfileRepository;
import suftware.tuitui.repository.TimeCapsuleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ProfileRepository profileRepository;
    private final TimeCapsuleRepository timeCapsuleRepository;

    //  전체 댓글 리스트 조회
    public List<CommentResponseDto> getCommentList(){
        List<Comment> commentList =  commentRepository.findAll();
        List<CommentResponseDto> commentRequestDtoList = new ArrayList<>();

        for (Comment comment : commentList){
            commentRequestDtoList.add(CommentResponseDto.toDTO(comment));
        }

        return commentRequestDtoList;
    }

    //  캡슐에 해당하는 모든 댓글 조회
    public List<CommentResponseDto> getCapsuleComment(Integer id){
        List<Comment> commentList =  commentRepository.findByTimeCapsule_TimeCapsuleId(id, Sort.by(Sort.Order.asc("writeAt")));
        List<CommentResponseDto> commentRequestDtoList = new ArrayList<>();

        for (Comment comment : commentList){
            commentRequestDtoList.add(CommentResponseDto.toDTO(comment));
        }

        return commentRequestDtoList;
    }

    //  댓글 저장
    public Optional<CommentResponseDto> saveCapsuleComment(CommentRequestDto commentRequestDto){
        Optional<Profile> profile = profileRepository.findById(commentRequestDto.getProfileId());
        Optional<TimeCapsule> timeCapsule = timeCapsuleRepository.findById(commentRequestDto.getTimeCapsuleId());

        if (profile.isEmpty() || timeCapsule.isEmpty()){
            return Optional.empty();
        }
        else {
            Comment comment = commentRepository.save(CommentRequestDto.toEntity(commentRequestDto, profile.get(), timeCapsule.get()));
            return Optional.of(CommentResponseDto.toDTO(comment));
        }
    }

    //  댓글 수정
    @Transactional
    public Optional<CommentResponseDto> updateCapsuleComment(Integer id, CommentRequestDto commentRequestDto){
        Optional<Comment> comment = commentRepository.findById(id);

        if (comment.isEmpty()){
            return Optional.empty();
        }
        else {
            comment.get().setComment(commentRequestDto.getComment());
            comment.get().setModified(Boolean.TRUE);
            return Optional.of(CommentResponseDto.toDTO(comment.get()));
        }
    }

    //  댓글 삭제
    @Transactional
    public Message deleteCapsuleComment(Integer id){
        Message message = new Message();

        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id);
            message.setStatus(HttpStatus.OK.value());
            message.setMessage(MsgCode.DELETE_SUCCESS.getMsg());
            return message;
        }
        else {
            message.setStatus(HttpStatus.NOT_FOUND.value());
            message.setMessage("commentId: " + id + ", " + MsgCode.COMMENT_NOT_FOUND);
            return message;
        }
    }
}
