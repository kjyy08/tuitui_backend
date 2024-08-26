package suftware.tuitui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import suftware.tuitui.domain.UserToken;


@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE " +
            "FROM UserToken u " +
            "WHERE u.account = :account")
    void deleteByAccount(@Param("account") String account);
    boolean existsByAccount(String account);

    @Modifying
    @Transactional
    @Query("DELETE " +
            "FROM UserToken u " +
            "WHERE u.refresh = :token")
    void deleteByRefresh(@Param("token") String token);
    boolean existsByRefresh(String token);
}
