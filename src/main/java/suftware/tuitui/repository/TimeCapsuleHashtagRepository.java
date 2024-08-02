package suftware.tuitui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import suftware.tuitui.domain.Hashtag;
import suftware.tuitui.domain.TimeCapsuleHashtag;

import java.util.List;

public interface TimeCapsuleHashtagRepository extends JpaRepository<TimeCapsuleHashtag, Integer> {

}
