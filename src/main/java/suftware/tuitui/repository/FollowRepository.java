package suftware.tuitui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import suftware.tuitui.domain.Follow;
import suftware.tuitui.dto.response.FollowDto;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Integer> {
    @Query("SELECT " +
            "NEW suftware.tuitui.dto.response.FollowDto(p.profileId, p.nickname, u.name, p.profileImgPath) " +
            "FROM Follow f " +
            "LEFT JOIN Profile p ON p.profileId = f.following.profileId " +
            "LEFT JOIN User u ON u.userId = f.following.user.userId " +
            "WHERE f.follower.profileId = :followerId")
    List<FollowDto> findByFollower(@Param("followerId") Integer id);

    @Query("SELECT " +
            "NEW suftware.tuitui.dto.response.FollowDto(p.profileId, p.nickname, u.name, profileImgPath) " +
            "FROM Follow f " +
            "LEFT JOIN Profile p ON p.profileId = f.follower.profileId " +
            "LEFT JOIN User u ON u.userId = f.follower.user.userId " +
            "WHERE f.following.profileId = :followingId")
    List<FollowDto> findByFollowing(@Param("followingId") Integer id);


}
