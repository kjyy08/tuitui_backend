package suftware.tuitui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import suftware.tuitui.domain.ProfileImage;

import java.util.Optional;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Integer> {
    @Query("SELECT pi " +
            "FROM ProfileImage pi " +
            "WHERE pi.profile.profileId = :profileId")
    Optional<ProfileImage> findByProfileId(@Param("profileId") Integer id);

}
