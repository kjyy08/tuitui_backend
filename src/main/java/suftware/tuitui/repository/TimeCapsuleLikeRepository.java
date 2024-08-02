package suftware.tuitui.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import suftware.tuitui.domain.Profile;
import suftware.tuitui.domain.TimeCapsuleLike;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeCapsuleLikeRepository extends JpaRepository<TimeCapsuleLike, Long> {
    public List<TimeCapsuleLike> findByTimeCapsule_TimeCapsuleId(Integer id);
    public boolean existsByProfile_ProfileIdAndTimeCapsule_TimeCapsuleId(Integer userId, Integer capsuleId);
}
