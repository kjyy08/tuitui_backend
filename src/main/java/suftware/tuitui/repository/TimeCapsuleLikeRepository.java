package suftware.tuitui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import suftware.tuitui.domain.TimeCapsuleLike;
import java.util.List;

@Repository
public interface TimeCapsuleLikeRepository extends JpaRepository<TimeCapsuleLike, Long> {
    List<TimeCapsuleLike> findByTimeCapsule_TimeCapsuleId(Integer id);

    //  count 성능 이슈로 사용 x
    //  @Query("SELECT " +
    //          "CASE WHEN COUNT(tcl) > 0" +
    //          "THEN TRUE ELSE FALSE END " +
    //          "FROM TimeCapsuleLike tcl " +
    //          "WHERE tcl.profile.profileId = :userId " +
    //          "AND tcl.timeCapsule.timeCapsuleId = :capsuleId")
    //  public boolean existsByProfileAndTimeCapsule(@Param("userId") Integer userId, @Param("capsuleId") Integer capsuleId);

    boolean existsByProfile_ProfileIdAndTimeCapsule_TimeCapsuleId(@Param("userId") Integer userId, @Param("capsuleId") Integer capsuleId);
}
