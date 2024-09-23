package suftware.tuitui.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import suftware.tuitui.domain.TimeCapsule;

import java.math.BigDecimal;

@Repository
public interface TimeCapsuleRepository extends JpaRepository<TimeCapsule, Integer> {
    Page<TimeCapsule> findByProfile_ProfileId(Integer id, Pageable pageable);
    Page<TimeCapsule> findByProfile_Nickname(String nickname, Pageable pageable);

    @Query(value = "SELECT ti.*, " +
            "(6371 * ACOS(COS(RADIANS(:latitude)) * COS(RADIANS(ti.latitude)) " +
            "* COS(RADIANS(ti.longitude) - RADIANS(:longitude)) " +
            "+ SIN(RADIANS(:latitude)) * SIN(RADIANS(ti.latitude)))) AS distance " +
            "FROM TimeCapsule ti " +
            "HAVING distance < :radius",
            countQuery = "SELECT COUNT(*) FROM TimeCapsule ti " +
                    "WHERE (6371 * ACOS(COS(RADIANS(:latitude)) * COS(RADIANS(ti.latitude)) " +
                    "* COS(RADIANS(ti.longitude) - RADIANS(:longitude)) " +
                    "+ SIN(RADIANS(:latitude)) * SIN(RADIANS(ti.latitude)))) < :radius",
            nativeQuery = true)
    Page<TimeCapsule> findByPosition(@Param("latitude") BigDecimal latitude,
                                     @Param("longitude") BigDecimal longitude,
                                     @Param("radius") Double radius,
                                     Pageable pageable);
}
