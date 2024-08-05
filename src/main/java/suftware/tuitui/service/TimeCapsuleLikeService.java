package suftware.tuitui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suftware.tuitui.common.exception.CustomException;
import suftware.tuitui.common.http.Message;
import suftware.tuitui.common.enumType.MsgCode;
import suftware.tuitui.domain.Profile;
import suftware.tuitui.domain.TimeCapsule;
import suftware.tuitui.domain.TimeCapsuleLike;
import suftware.tuitui.dto.request.TimeCapsuleLikeRequestDto;
import suftware.tuitui.dto.response.ProfileResponseDto;
import suftware.tuitui.dto.response.TimeCapsuleLikeResponseDto;
import suftware.tuitui.repository.ProfileRepository;
import suftware.tuitui.repository.TimeCapsuleLikeRepository;
import suftware.tuitui.repository.TimeCapsuleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimeCapsuleLikeService {
    private final TimeCapsuleLikeRepository timeCapsuleLikeRepository;
    private final TimeCapsuleRepository timeCapsuleRepository;
    private final ProfileRepository profileRepository;

    //  캡슐 좋아요를 누른 유저 목록 조회
    public List<ProfileResponseDto> getCapsuleLike(Integer id){
        List<TimeCapsuleLike> timeCapsuleLikeList = timeCapsuleLikeRepository.findByTimeCapsule_TimeCapsuleId(id);
        List<ProfileResponseDto> profileResponseDtoList = new ArrayList<>();

        for(TimeCapsuleLike capsuleLike : timeCapsuleLikeList){
            profileResponseDtoList.add(ProfileResponseDto.toDTO(capsuleLike.getProfile()));
        }

        return profileResponseDtoList;
    }

    //  캡슐 좋아요 저장
    public Optional<TimeCapsuleLikeResponseDto> saveCapsuleLike(TimeCapsuleLikeRequestDto timeCapsuleLikeRequestDto){
        Profile profile = profileRepository.findById(timeCapsuleLikeRequestDto.getProfileId())
                .orElseThrow(() -> new CustomException(MsgCode.PROFILE_NOT_FOUND));
        TimeCapsule timeCapsule = timeCapsuleRepository.findById(timeCapsuleLikeRequestDto.getTimeCapsuleId())
                .orElseThrow(() -> new CustomException(MsgCode.CAPSULE_NOT_FOUND));

        if (!timeCapsuleLikeRepository.existsByProfile_ProfileIdAndTimeCapsule_TimeCapsuleId(profile.getProfileId(), timeCapsule.getTimeCapsuleId())){
            TimeCapsuleLike timeCapsuleLike = timeCapsuleLikeRepository.save(TimeCapsuleLikeRequestDto.toEntity(timeCapsule, profile));
            return Optional.of(TimeCapsuleLikeResponseDto.toDto(timeCapsuleLike));
        }
        else {
            throw new CustomException(MsgCode.CAPSULE_LIKE_EXIST);
        }
    }

    //  캡슐 좋아요 삭제
    @Transactional
    public void deleteCapsuleLike(Integer id) {
        if (!timeCapsuleLikeRepository.existsById(id.longValue())) {
            throw new CustomException(MsgCode.CAPSULE_LIKE_NOT_FOUND);
        }

        timeCapsuleLikeRepository.deleteById(id.longValue());
    }
}
