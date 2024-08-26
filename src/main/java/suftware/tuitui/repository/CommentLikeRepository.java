package suftware.tuitui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import suftware.tuitui.domain.CommentLike;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Integer> {
    @Query("SELECT cl " +
            "FROM CommentLike cl " +
            "WHERE cl.comment.commentId = :commentId")
    List<CommentLike> findByCommentId(@Param("commentId") Integer id);

    // 댓글에 좋아요 눌렀는지 체크
    boolean existsByComment_CommentIdAndProfile_ProfileId(Integer commentId, Integer userId);

    //  댓글 id와 프로필 id에 맞는 좋아요 조회
    Optional<CommentLike> findByComment_CommentIdAndProfile_ProfileId(Integer commentId, Integer userId);

}
