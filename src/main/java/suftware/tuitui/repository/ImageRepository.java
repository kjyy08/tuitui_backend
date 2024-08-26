package suftware.tuitui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import suftware.tuitui.domain.Image;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    //  이미지 이름 기준 조회
    Optional<Image> findByImageName(String name);
    //  이미지 이름에 해당하는 이미지가 있는지 확인
    boolean existsByImageName(String name);
    //  timeCapsule id에 일치하는 이미지 리스트로 반환
    List<Image> findByTimeCapsule_TimeCapsuleId(Integer id);


}
