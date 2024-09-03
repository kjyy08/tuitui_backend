package suftware.tuitui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import suftware.tuitui.domain.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByAccount(String account);

    Optional<User> findByUserIdAndAccount(Integer id, String account);

    boolean existsByAccount(String account);


    void deleteByAccount(String account);
}
