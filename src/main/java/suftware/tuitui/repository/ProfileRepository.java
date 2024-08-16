package suftware.tuitui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import suftware.tuitui.domain.Profile;
import suftware.tuitui.domain.User;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    Optional<Profile> findByNickname(String nickname);

    Optional<Profile> findByUser_UserId(Integer id);
    boolean existsByUser_UserId(Integer id);
    boolean existsByNickname(String nickname);
}
