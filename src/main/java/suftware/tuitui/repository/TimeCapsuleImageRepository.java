package suftware.tuitui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import suftware.tuitui.domain.TimeCapsuleImage;

import java.util.List;

public interface TimeCapsuleImageRepository extends JpaRepository<TimeCapsuleImage, Integer> {
    @Query("SELECT tci " +
            "FROM TimeCapsuleImage tci " +
            "WHERE tci.timeCapsule.timeCapsuleId = :capsuleId")
    List<TimeCapsuleImage> findByCapsuleId(@Param("capsuleId") Integer id);

    @Query("SELECT tci " +
            "FROM TimeCapsuleImage tci " +
            "WHERE tci.timeCapsule.profile.user.userId = :userId")
    List<TimeCapsuleImage> findByUserId(@Param("userId") Integer id);


}
