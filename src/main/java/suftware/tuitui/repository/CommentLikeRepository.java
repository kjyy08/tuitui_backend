package suftware.tuitui.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import suftware.tuitui.domain.CommentLike;
import suftware.tuitui.domain.TimeCapsuleLike;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Integer>{
    // 좋아요 리스트
    List<CommentLike> findByComment_CommentId(Integer id);

    // 댓글에 좋아요 눌렀는지 체크
    boolean existsByComment_CommentIdAndProfile_ProfileId(Integer commentId, Integer userId);
    
    // 댓글 좋아요 삭제
    void deleteByComment_CommentIdAndProfile_ProfileId(Integer commentId, Integer userId);
}
