package suftware.tuitui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suftware.tuitui.config.http.Message;
import suftware.tuitui.config.http.MsgCode;
import suftware.tuitui.domain.Profile;
import suftware.tuitui.domain.TimeCapsule;
import suftware.tuitui.domain.TimeCapsuleLike;
import suftware.tuitui.dto.request.TimeCapsuleLikeRequestDto;
import suftware.tuitui.dto.response.ProfileResponseDto;
import suftware.tuitui.dto.response.TimeCapsuleLikeResponseDto;
import suftware.tuitui.repository.ProfileRepository;
import suftware.tuitui.repository.TimeCapsuleLikeRepository;
import suftware.tuitui.repository.TimeCapsuleRepository;

import java.math.BigInteger;
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
        Optional<TimeCapsule> timeCapsule = timeCapsuleRepository.findById(timeCapsuleLikeRequestDto.getTimeCapsuleId());
        Optional<Profile> profile = profileRepository.findById(timeCapsuleLikeRequestDto.getProfileId());

        if (!timeCapsuleLikeRepository.existsByProfile_ProfileIdAndTimeCapsule_TimeCapsuleId(profile.get().getProfileId(), timeCapsule.get().getTimeCapsuleId())){
            TimeCapsuleLike timeCapsuleLike = timeCapsuleLikeRepository.save(TimeCapsuleLikeRequestDto.toEntity(timeCapsule.get(), profile.get()));
            return Optional.of(TimeCapsuleLikeResponseDto.toDto(timeCapsuleLike));
        }
        else {
            return Optional.empty();
        }
    }

    //  캡슐 좋아요 삭제
    @Transactional
    public Message deleteCapsuleLike(Integer id){
        Message message = new Message();

        if (timeCapsuleLikeRepository.existsById(id.longValue())) {
            timeCapsuleLikeRepository.deleteById(id.longValue());
            message.setStatus(HttpStatus.OK.value());
            message.setMessage(MsgCode.DELETE_SUCCESS.getMsg());
            return message;
        }
        else {
            message.setStatus(HttpStatus.NOT_FOUND.value());
            message.setMessage("capsuleLikeId: " + id + ", " + MsgCode.READ_FAIL.getMsg());
            return message;
        }
    }
}
