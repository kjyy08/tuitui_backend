package suftware.tuitui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import suftware.tuitui.domain.Hashtag;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Integer> {


}
