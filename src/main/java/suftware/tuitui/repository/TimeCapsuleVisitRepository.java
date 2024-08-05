package suftware.tuitui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import suftware.tuitui.domain.TimeCapsuleVisit;

import java.util.Optional;

@Repository
public interface TimeCapsuleVisitRepository extends JpaRepository<TimeCapsuleVisit, Long> {
    public Optional<TimeCapsuleVisit> findByTimeCapsule_TimeCapsuleId(Integer id);
}
