package suftware.tuitui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import suftware.tuitui.domain.Follow;
import suftware.tuitui.dto.response.FollowDto;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Integer> {
    @Query("SELECT " +
            "NEW suftware.tuitui.dto.response.FollowDto(p.profileId, p.nickname, p.name, p.profileImgPath) " +
            "FROM Follow f " +
            "LEFT JOIN Profile p ON p.profileId = f.following.profileId " +
            "WHERE f.follower.profileId = :followerId")
    List<FollowDto> findByFollower(@Param("followerId") Integer id);

    @Query("SELECT " +
            "NEW suftware.tuitui.dto.response.FollowDto(p.profileId, p.nickname, p.name, profileImgPath) " +
            "FROM Follow f " +
            "LEFT JOIN Profile p ON p.profileId = f.follower.profileId " +
            "WHERE f.following.profileId = :followingId")
    List<FollowDto> findByFollowing(@Param("followingId") Integer id);

    @Modifying
    @Query("DELETE " +
            "FROM Follow f " +
            "WHERE f.follower.profileId = :follower " +
            "AND f.following.profileId = :following")
    void deleteById(@Param("follower") Integer follower, @Param("following") Integer following);

    boolean existsByFollower_ProfileIdAndFollowing_ProfileId(Integer follower, Integer following);
}
