package suftware.tuitui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suftware.tuitui.common.exception.CustomException;
import suftware.tuitui.common.enumType.MsgCode;
import suftware.tuitui.domain.Image;
import suftware.tuitui.domain.Profile;
import suftware.tuitui.domain.TimeCapsule;
import suftware.tuitui.dto.request.TimeCapsuleRequestDto;
import suftware.tuitui.dto.response.ImageResponseDto;
import suftware.tuitui.dto.response.TimeCapsuleResponseDto;
import suftware.tuitui.repository.ImageRepository;
import suftware.tuitui.repository.ProfileRepository;
import suftware.tuitui.repository.TimeCapsuleRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class TimeCapsuleService {
    private final TimeCapsuleRepository timeCapsuleRepository;
    private final ProfileRepository profileRepository;
    private final ImageRepository imageRepository;

    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

    private List<ImageResponseDto> getCapsuleImageList(Integer id){
        List<Image> imageList = imageRepository.findByTimeCapsule_TimeCapsuleId(id);

        //  캡슐에 저장된 이미지가 1개 이상일 경우 이미지 포함 반환
        if (!imageList.isEmpty()) {
            List<ImageResponseDto> imageResponseDtoList = new ArrayList<>();

            for (Image image : imageList) {
                imageResponseDtoList.add(ImageResponseDto.toDto(image));
            }

            return imageResponseDtoList;
        }

        //  없는 경우 null
        return null;
    }

    //  전체 캡슐 조회
    @Transactional(readOnly = true)
    public List<TimeCapsuleResponseDto> getCapsuleList() {
        List<TimeCapsule> timeCapsuleList = timeCapsuleRepository.findAll();

        if (timeCapsuleList.isEmpty()){
            throw new CustomException(MsgCode.CAPSULE_NOT_FOUND);
        }

        List<TimeCapsuleResponseDto> timeCapsuleResponseDtoList = new ArrayList<>();

        for (TimeCapsule timeCapsule : timeCapsuleList){
            List<ImageResponseDto> imageResponseDtoList = getCapsuleImageList(timeCapsule.getTimeCapsuleId());

            if (imageResponseDtoList != null) {
                timeCapsuleResponseDtoList.add(TimeCapsuleResponseDto.toDTO(timeCapsule, imageResponseDtoList));
            }
            else {
                timeCapsuleResponseDtoList.add(TimeCapsuleResponseDto.toDTO(timeCapsule));
            }
        }

        return timeCapsuleResponseDtoList;
    }

    //  캡슐 id 기준 조회
    @Transactional(readOnly = true)
    public Optional<TimeCapsuleResponseDto> getCapsule(Integer id){
        TimeCapsule timeCapsule = timeCapsuleRepository.findById(id)
                .orElseThrow(() -> new CustomException(MsgCode.CAPSULE_NOT_FOUND));

        List<ImageResponseDto> imageResponseDtoList = getCapsuleImageList(timeCapsule.getTimeCapsuleId());

        if (imageResponseDtoList != null) {
            return Optional.of(TimeCapsuleResponseDto.toDTO(timeCapsule, imageResponseDtoList));
        }
        else {
            return Optional.of(TimeCapsuleResponseDto.toDTO(timeCapsule));
        }
    }

    //  글쓴이 id 기준 조회
    @Transactional(readOnly = true)
    public List<TimeCapsuleResponseDto> getCapsuleByWriteUser(Integer id) {
        List<TimeCapsule> timeCapsuleList = timeCapsuleRepository.findByProfile_ProfileId(id);

        if (timeCapsuleList.isEmpty()){
            throw new CustomException(MsgCode.CAPSULE_NOT_FOUND);
        }

        List<TimeCapsuleResponseDto> timeCapsuleResponseDtoList = new ArrayList<>();

        for (TimeCapsule timeCapsule : timeCapsuleList){
            List<ImageResponseDto> imageResponseDtoList = getCapsuleImageList(timeCapsule.getTimeCapsuleId());

            if (imageResponseDtoList != null) {
                timeCapsuleResponseDtoList.add(TimeCapsuleResponseDto.toDTO(timeCapsule, imageResponseDtoList));
            }
            else {
                timeCapsuleResponseDtoList.add(TimeCapsuleResponseDto.toDTO(timeCapsule));
            }
        }

        return timeCapsuleResponseDtoList;
    }

    //  해당 닉네임의 캡슐 목록 조회
    @Transactional(readOnly = true)
    public List<TimeCapsuleResponseDto> getCapsuleByNickname(String nickname) {
        List<TimeCapsule> timeCapsuleList = timeCapsuleRepository.findByProfile_Nickname(nickname);

        if (timeCapsuleList.isEmpty()){
            throw new CustomException(MsgCode.CAPSULE_NOT_FOUND);
        }

        List<TimeCapsuleResponseDto> timeCapsuleResponseDtoList = new ArrayList<>();

        for (TimeCapsule timeCapsule : timeCapsuleList){
            List<ImageResponseDto> imageResponseDtoList = getCapsuleImageList(timeCapsule.getTimeCapsuleId());

            if (imageResponseDtoList != null) {
                timeCapsuleResponseDtoList.add(TimeCapsuleResponseDto.toDTO(timeCapsule, imageResponseDtoList));
            }
            else {
                timeCapsuleResponseDtoList.add(TimeCapsuleResponseDto.toDTO(timeCapsule));
            }
        }

        return timeCapsuleResponseDtoList;
    }

    //  캡슐 저장
    public Optional<TimeCapsuleResponseDto> save(TimeCapsuleRequestDto timeCapsuleRequestDto){
        logger.debug("TimeCapsuleService.save ---------- Saving TimeCapsule with content: {}, location: {}, writeUserId: {}, remindDate: {}",
                timeCapsuleRequestDto.getContent(),
                timeCapsuleRequestDto.getLocation(),
                timeCapsuleRequestDto.getWriteUser(),
                timeCapsuleRequestDto.getRemindDate());

        Profile writeUser = profileRepository.findByNickname(timeCapsuleRequestDto.getWriteUser())
                    .orElseThrow(() -> new CustomException(MsgCode.PROFILE_NOT_FOUND));

        TimeCapsule timeCapsule = timeCapsuleRepository.save(TimeCapsuleRequestDto.toEntity(timeCapsuleRequestDto, writeUser));

        return Optional.of(TimeCapsuleResponseDto.toDTO(timeCapsule));
    }

    //  타임캡슐 수정
    @Transactional
    public Optional<TimeCapsuleResponseDto> updateCapsule(Integer id, TimeCapsuleRequestDto timeCapsuleRequestDto) {
        TimeCapsule timeCapsule = timeCapsuleRepository.findById(id)
                .orElseThrow(() -> new CustomException(MsgCode.CAPSULE_NOT_FOUND));

        if (!(timeCapsuleRequestDto.getContent() == null))
            timeCapsule.setContent(timeCapsuleRequestDto.getContent());
        if (!(timeCapsuleRequestDto.getRemindDate() == null))
            timeCapsule.setRemindDate(timeCapsuleRequestDto.getRemindDate());
        timeCapsule.setUpdateAt(new Timestamp(System.currentTimeMillis()));

        return Optional.of(TimeCapsuleResponseDto.toDTO(timeCapsule));
    }

    //  타임캡슐 삭제
    @Transactional
    public void deleteCapsule(Integer id) {
        if (!timeCapsuleRepository.existsById(id)) {
            throw new CustomException(MsgCode.CAPSULE_NOT_FOUND);
        }

        timeCapsuleRepository.deleteById(id);
    }
}
