package suftware.tuitui.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import suftware.tuitui.domain.TimeCapsule;

@Repository
public interface TimeCapsuleRepository extends JpaRepository<TimeCapsule, Integer> {
    Page<TimeCapsule> findByProfile_ProfileId(Integer id, Pageable pageable);
    Page<TimeCapsule> findByProfile_Nickname(String nickname, Pageable pageable);

}
