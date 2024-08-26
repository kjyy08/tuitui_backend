package suftware.tuitui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import suftware.tuitui.domain.TimeCapsule;

import java.util.List;

@Repository
public interface TimeCapsuleRepository extends JpaRepository<TimeCapsule, Integer> {
    List<TimeCapsule> findByProfile_ProfileId(Integer id);
    List<TimeCapsule> findByProfile_Nickname(String nickname);

}
