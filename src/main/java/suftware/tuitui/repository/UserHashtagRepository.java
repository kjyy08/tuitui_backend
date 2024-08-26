package suftware.tuitui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import suftware.tuitui.domain.UserHashtag;

@Repository
public interface UserHashtagRepository extends JpaRepository<UserHashtag, Integer> {

}
