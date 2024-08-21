package suftware.tuitui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import suftware.tuitui.domain.UserToken;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Integer> {
    boolean existsByRefresh(String token);
    void deleteByRefresh(String token);
}
