package suftware.tuitui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import suftware.tuitui.domain.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByAccount(String account);

    Optional<User> findByAccountAndPassword(String account, String password);
    boolean existsByAccount(String account);

    boolean existsByAccountAndPassword(String account, String password);

    void deleteByAccount(String account);
}
