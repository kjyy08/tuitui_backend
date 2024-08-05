package suftware.tuitui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import suftware.tuitui.common.enumType.MsgCode;
import suftware.tuitui.common.exception.CustomException;
import suftware.tuitui.dto.response.FollowDto;
import suftware.tuitui.repository.FollowRepository;
import suftware.tuitui.repository.ProfileRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final ProfileRepository profileRepository;

    public List<FollowDto> getFollowerList(Integer id) {
        if (!profileRepository.existsById(id)) {
            throw new CustomException(MsgCode.PROFILE_NOT_FOUND);
        }

        List<FollowDto> followerList = followRepository.findByFollower(id);

        if (followerList.isEmpty()) {
            return null;
        }

        return followerList;
    }

    public List<FollowDto> getFollowingList(Integer id) {
        if (!profileRepository.existsById(id)) {
            throw new CustomException(MsgCode.PROFILE_NOT_FOUND);
        }

        List<FollowDto> followingList = followRepository.findByFollowing(id);

        if (followingList.isEmpty()) {
            return null;
        }

        return followingList;
    }
}
