package suftware.tuitui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import suftware.tuitui.domain.IpBlackList;

import java.util.Optional;

public interface IpBlackListRepository extends JpaRepository<IpBlackList, Integer> {
    Optional<IpBlackList> findByIpAddress(String address);

    boolean existsByIpAddress(String address);
}
