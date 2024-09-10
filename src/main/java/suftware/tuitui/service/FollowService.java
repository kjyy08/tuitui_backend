package suftware.tuitui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.common.exception.TuiTuiException;
import suftware.tuitui.domain.Profile;
import suftware.tuitui.dto.request.FollowRequestDto;
import suftware.tuitui.dto.response.FollowDto;
import suftware.tuitui.repository.FollowRepository;
import suftware.tuitui.repository.ProfileRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final ProfileRepository profileRepository;

    //  팔로워 리스트를 가져옴
    public List<FollowDto> getFollowerList(Integer id) {
        if (!profileRepository.existsById(id)) {
            throw new TuiTuiException(TuiTuiMsgCode.PROFILE_NOT_FOUND);
        }

        List<FollowDto> followerList = followRepository.findByFollower(id);

        if (followerList.isEmpty()) {
            return null;
        }

        return followerList;
    }

    //  팔로윙 리스트를 가져옴
    public List<FollowDto> getFollowingList(Integer id) {
        if (!profileRepository.existsById(id)) {
            throw new TuiTuiException(TuiTuiMsgCode.PROFILE_NOT_FOUND);
        }

        List<FollowDto> followingList = followRepository.findByFollowing(id);

        if (followingList.isEmpty()) {
            return null;
        }

        return followingList;
    }

    //  팔로우 저장
    public void saveFollow(FollowRequestDto followRequestDto){
        //  자기 자신을 팔로윙 한 경우 예외
        if (followRequestDto.getFollowerId().equals(followRequestDto.getFollowingId())){
            throw new TuiTuiException(TuiTuiMsgCode.FOLLOWS_NOT_ALLOWED);
        }

        if (!followRepository.existsByFollower_ProfileIdAndFollowing_ProfileId(followRequestDto.getFollowerId(),
                followRequestDto.getFollowingId())) {
            Profile follower = profileRepository.findById(followRequestDto.getFollowerId())
                    .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.PROFILE_NOT_FOUND));
            Profile following = profileRepository.findById(followRequestDto.getFollowingId())
                    .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.PROFILE_NOT_FOUND));

            followRepository.save(FollowRequestDto.toEntity(follower, following));
        }
        else {
            throw new TuiTuiException(TuiTuiMsgCode.FOLLOWS_EXIST);
        }
    }

    //  팔로우 삭제
    @Transactional
    public void deleteFollow(FollowRequestDto followRequestDto){
        if (followRepository.existsByFollower_ProfileIdAndFollowing_ProfileId(followRequestDto.getFollowerId(),
                followRequestDto.getFollowingId())) {
            Profile follower = profileRepository.findById(followRequestDto.getFollowerId())
                    .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.PROFILE_NOT_FOUND));
            Profile following = profileRepository.findById(followRequestDto.getFollowingId())
                    .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.PROFILE_NOT_FOUND));

            followRepository.deleteById(follower.getProfileId(), following.getProfileId());
        }
        else {
            throw new TuiTuiException(TuiTuiMsgCode.FOLLOWS_NOT_FOUND);
        }
    }
}
