package suftware.tuitui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import suftware.tuitui.domain.TimeCapsuleHashtag;

@Repository
public interface TimeCapsuleHashtagRepository extends JpaRepository<TimeCapsuleHashtag, Integer> {

}
