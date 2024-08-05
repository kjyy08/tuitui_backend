package suftware.tuitui.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import suftware.tuitui.domain.Profile;
import suftware.tuitui.domain.TimeCapsuleLike;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeCapsuleLikeRepository extends JpaRepository<TimeCapsuleLike, Long> {
    public List<TimeCapsuleLike> findByTimeCapsule_TimeCapsuleId(Integer id);

    //  count 성능 이슈로 사용 x
    //  @Query("SELECT " +
    //          "CASE WHEN COUNT(tcl) > 0" +
    //          "THEN TRUE ELSE FALSE END " +
    //          "FROM TimeCapsuleLike tcl " +
    //          "WHERE tcl.profile.profileId = :userId " +
    //          "AND tcl.timeCapsule.timeCapsuleId = :capsuleId")
    //  public boolean existsByProfileAndTimeCapsule(@Param("userId") Integer userId, @Param("capsuleId") Integer capsuleId);

    public boolean existsByProfile_ProfileIdAndTimeCapsule_TimeCapsuleId(@Param("userId") Integer userId, @Param("capsuleId") Integer capsuleId);
}
