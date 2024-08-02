package suftware.tuitui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import suftware.tuitui.domain.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public Optional<User> findByAccount(String account);

    public Optional<User> findByAccountAndPassword(String account, String password);
    public boolean existsByAccount(String account);

    public boolean existsByAccountAndPassword(String account, String password);

    public void deleteByAccount(String account);
}
