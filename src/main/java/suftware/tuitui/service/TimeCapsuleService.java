package suftware.tuitui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suftware.tuitui.config.http.Message;
import suftware.tuitui.config.http.MsgCode;
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
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimeCapsuleService {
    private final TimeCapsuleRepository timeCapsuleRepository;
    private final ProfileRepository profileRepository;
    private final ImageRepository imageRepository;

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
    public List<TimeCapsuleResponseDto> getCapsuleList() {
        List<TimeCapsule> timeCapsuleList = timeCapsuleRepository.findAll();
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
    public Optional<TimeCapsuleResponseDto> getCapsule(Integer id){
        TimeCapsule timeCapsule = timeCapsuleRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("TimeCapsule " + id + " Not Found"));

        List<ImageResponseDto> imageResponseDtoList = getCapsuleImageList(timeCapsule.getTimeCapsuleId());

        if (imageResponseDtoList != null) {
            return Optional.of(TimeCapsuleResponseDto.toDTO(timeCapsule, imageResponseDtoList));
        }
        else {
            return Optional.of(TimeCapsuleResponseDto.toDTO(timeCapsule));
        }
    }

    //  글쓴이 id 기준 조회
    public List<TimeCapsuleResponseDto> getCapsuleByWriteUser(Integer id) {
        List<TimeCapsule> timeCapsuleList = timeCapsuleRepository.findByProfile_ProfileId(id);
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
        Optional<Profile> writeUser = profileRepository.findByNickname(timeCapsuleRequestDto.getWriteUser());

        if (writeUser.isEmpty()){
            return Optional.empty();
        }

        TimeCapsule timeCapsule = timeCapsuleRepository.save(TimeCapsuleRequestDto.toEntity(timeCapsuleRequestDto, writeUser.get()));
        return Optional.of(TimeCapsuleResponseDto.toDTO(timeCapsule));
    }

    //  해당 닉네임의 캡슐 목록 조회
    public List<TimeCapsuleResponseDto> getCapsuleByNickname(String nickname) {
        List<TimeCapsule> timeCapsuleList = timeCapsuleRepository.findByProfile_Nickname(nickname);
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

    //  타임캡슐 수정
    @Transactional
    public Optional<TimeCapsuleResponseDto> updateCapsule(Integer id, TimeCapsuleRequestDto timeCapsuleRequestDto){
        Optional<TimeCapsule> timeCapsule = timeCapsuleRepository.findById(id);

        if (timeCapsule.isEmpty()){
            return Optional.empty();
        }
        else {
            timeCapsule.get().setContent(timeCapsuleRequestDto.getContent());
            timeCapsule.get().setRemindDate(timeCapsuleRequestDto.getRemindDate());
            timeCapsule.get().setUpdateAt(new Timestamp(System.currentTimeMillis()));

            return Optional.of(TimeCapsuleResponseDto.toDTO(timeCapsule.get()));
        }
    }

    //  타임캡슐 삭제
    @Transactional
    public Message deleteCapsule(Integer id){
        Message message = new Message();

        if (timeCapsuleRepository.existsById(id)){
            timeCapsuleRepository.deleteById(id);
            message.setStatus(HttpStatus.OK.value());
            message.setMessage(MsgCode.DELETE_SUCCESS.getMsg());
            return message;
        }
        else {
            message.setStatus(HttpStatus.NOT_FOUND.value());
            message.setMessage("capsuleId: " + id + ", " + MsgCode.CAPSULE_NOT_FOUND.getMsg());
            return message;
        }
    }
}
