package suftware.tuitui.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suftware.tuitui.common.exception.TuiTuiException;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.common.time.DateTimeUtil;
import suftware.tuitui.domain.Profile;
import suftware.tuitui.domain.TimeCapsule;
import suftware.tuitui.domain.TimeCapsuleImage;
import suftware.tuitui.dto.request.TimeCapsuleRequestDto;
import suftware.tuitui.dto.response.ImageResponseDto;
import suftware.tuitui.dto.response.PageResponse;
import suftware.tuitui.dto.response.TimeCapsuleResponseDto;
import suftware.tuitui.repository.ProfileRepository;
import suftware.tuitui.repository.TimeCapsuleImageRepository;
import suftware.tuitui.repository.TimeCapsuleRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimeCapsuleService {
    private final TimeCapsuleRepository timeCapsuleRepository;
    private final TimeCapsuleImageRepository timeCapsuleImageRepository;
    private final ProfileRepository profileRepository;

    @Transactional(readOnly = true)
    private List<ImageResponseDto> getCapsuleImageList(Integer id){
        List<TimeCapsuleImage> imageList = timeCapsuleImageRepository.findByCapsuleId(id);

        return imageList.stream()
                .map(ImageResponseDto::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<PageResponse> getCapsuleList(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<TimeCapsule> capsulePage = timeCapsuleRepository.findAll(pageable);

        List<TimeCapsule> timeCapsuleList = capsulePage.getContent();

        // List of TimeCapsuleResponseDto 생성
        List<TimeCapsuleResponseDto> timeCapsuleResponseDtoList = timeCapsuleList.stream()
                .map(timeCapsule -> {
                    List<ImageResponseDto> imageResponseDtoList = getCapsuleImageList(timeCapsule.getTimeCapsuleId());
                    return imageResponseDtoList.isEmpty()
                            ? TimeCapsuleResponseDto.toDTO(timeCapsule)
                            : TimeCapsuleResponseDto.toDTO(timeCapsule, imageResponseDtoList);
                })
                .toList();

        // 페이지 응답 생성 후 Optional로 반환
        return Optional.of(PageResponse.builder()
                .contents(timeCapsuleResponseDtoList)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalElements(Long.valueOf(capsulePage.getTotalElements()).intValue())
                .totalPages(capsulePage.getTotalPages())
                .lastPage(capsulePage.isLast())
                .build());
    }

    //  캡슐 id 기준 조회
    @Transactional(readOnly = true)
    public Optional<TimeCapsuleResponseDto> getCapsule(Integer id){
        TimeCapsule timeCapsule = timeCapsuleRepository.findById(id)
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.CAPSULE_NOT_FOUND));

        List<ImageResponseDto> imageResponseDtoList = getCapsuleImageList(timeCapsule.getTimeCapsuleId());

        return Optional.of(imageResponseDtoList.isEmpty()
                ? TimeCapsuleResponseDto.toDTO(timeCapsule)
                : TimeCapsuleResponseDto.toDTO(timeCapsule, imageResponseDtoList));
    }

    // 프로필 id 기준 조회
    @Transactional(readOnly = true)
    public Optional<PageResponse> getCapsuleByWriteUser(Integer profileId, Integer pageNo, Integer pageSize, String sortBy) {
        if (!profileRepository.existsById(profileId)) {
            throw new TuiTuiException(TuiTuiMsgCode.PROFILE_NOT_FOUND);
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<TimeCapsule> capsulePage = timeCapsuleRepository.findByProfile_ProfileId(profileId, pageable);

        List<TimeCapsule> timeCapsuleList = capsulePage.getContent();

        if (timeCapsuleList.isEmpty()) {
            throw new TuiTuiException(TuiTuiMsgCode.CAPSULE_NOT_FOUND);
        }

        List<TimeCapsuleResponseDto> timeCapsuleResponseDtoList = timeCapsuleList.stream()
                .map(timeCapsule -> {
                    List<ImageResponseDto> imageResponseDtoList = getCapsuleImageList(timeCapsule.getTimeCapsuleId());
                    return imageResponseDtoList.isEmpty()
                            ? TimeCapsuleResponseDto.toDTO(timeCapsule)
                            : TimeCapsuleResponseDto.toDTO(timeCapsule, imageResponseDtoList);
                })
                .toList();

        return Optional.of(PageResponse.builder()
                .contents(timeCapsuleResponseDtoList)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalElements(Long.valueOf(capsulePage.getTotalElements()).intValue())
                .totalPages(capsulePage.getTotalPages())
                .lastPage(capsulePage.isLast())
                .build());
    }


    //  해당 닉네임의 캡슐 목록 조회
    @Transactional(readOnly = true)
    public Optional<PageResponse> getCapsuleByNickname(String nickname, Integer pageNo, Integer pageSize, String sortBy) {
        if (!profileRepository.existsByNickname(nickname)) {
            throw new TuiTuiException(TuiTuiMsgCode.PROFILE_NOT_FOUND);
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<TimeCapsule> capsulePage = timeCapsuleRepository.findByProfile_Nickname(nickname, pageable);

        List<TimeCapsule> timeCapsuleList = capsulePage.getContent();

        if (timeCapsuleList.isEmpty()) {
            throw new TuiTuiException(TuiTuiMsgCode.CAPSULE_NOT_FOUND);
        }

        List<TimeCapsuleResponseDto> timeCapsuleResponseDtoList = timeCapsuleList.stream()
                .map(timeCapsule -> {
                    List<ImageResponseDto> imageResponseDtoList = getCapsuleImageList(timeCapsule.getTimeCapsuleId());
                    return imageResponseDtoList.isEmpty()
                            ? TimeCapsuleResponseDto.toDTO(timeCapsule)
                            : TimeCapsuleResponseDto.toDTO(timeCapsule, imageResponseDtoList);
                })
                .toList();

        return Optional.of(PageResponse.builder()
                .contents(timeCapsuleResponseDtoList)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalElements(Long.valueOf(capsulePage.getTotalElements()).intValue())
                .totalPages(capsulePage.getTotalPages())
                .lastPage(capsulePage.isLast())
                .build());
    }

    @Transactional(readOnly = true)
    public Optional<PageResponse> getCapsuleByPosition(BigDecimal latitude, BigDecimal longitude, Double radius,
                                                       Integer pageNo, Integer pageSize, String sortBy) {
        // 정렬 문자열 변환
        Map<String, String> sortByMap = Map.of(
                "timeCapsuleId", "capsule_id",
                "profile", "write_user_id",
                "writeAt", "write_at",
                "updateAt", "update_at",
                "remindDate", "remind_date"
        );

        //  변환
        sortBy = sortByMap.getOrDefault(sortBy, sortBy);

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<TimeCapsule> capsulePage = timeCapsuleRepository.findByPosition(latitude, longitude, radius, pageable);

        List<TimeCapsule> timeCapsuleList = capsulePage.getContent();

        if (timeCapsuleList.isEmpty()) {
            throw new TuiTuiException(TuiTuiMsgCode.CAPSULE_NOT_FOUND);
        }

        List<TimeCapsuleResponseDto> timeCapsuleResponseDtoList = timeCapsuleList.stream()
                .map(timeCapsule -> {
                    List<ImageResponseDto> imageResponseDtoList = getCapsuleImageList(timeCapsule.getTimeCapsuleId());
                    return imageResponseDtoList.isEmpty()
                            ? TimeCapsuleResponseDto.toDTO(timeCapsule)
                            : TimeCapsuleResponseDto.toDTO(timeCapsule, imageResponseDtoList);
                })
                .toList();

        return Optional.of(PageResponse.builder()
                .contents(timeCapsuleResponseDtoList)
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalElements(Long.valueOf(capsulePage.getTotalElements()).intValue())
                .totalPages(capsulePage.getTotalPages())
                .lastPage(capsulePage.isLast())
                .build());
    }


    //  캡슐 저장
    public Optional<TimeCapsuleResponseDto> save(TimeCapsuleRequestDto timeCapsuleRequestDto){
        log.info("TimeCapsuleService.save() -> dto received, request dto: {}", timeCapsuleRequestDto);

        Profile profile = profileRepository.findById(timeCapsuleRequestDto.getProfileId())
                    .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.PROFILE_NOT_FOUND));

        TimeCapsule timeCapsule = timeCapsuleRepository.save(TimeCapsuleRequestDto.toEntity(timeCapsuleRequestDto, profile));

        TimeCapsuleResponseDto responseDto = TimeCapsuleResponseDto.toDTO(timeCapsule);
        log.info("TimeCapsuleService.save() -> success, response data: {}", responseDto);

        return Optional.of(responseDto);
    }

    //  타임캡슐 수정
    @Transactional
    public Optional<TimeCapsuleResponseDto> updateCapsule(Integer id, TimeCapsuleRequestDto timeCapsuleRequestDto) {
        TimeCapsule timeCapsule = timeCapsuleRepository.findById(id)
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.CAPSULE_NOT_FOUND));

        if (!(timeCapsuleRequestDto.getContent() == null))
            timeCapsule.updateContent(timeCapsuleRequestDto.getContent());

        if (!(timeCapsuleRequestDto.getRemindDate() == null))
            timeCapsule.updateRemindDate(timeCapsuleRequestDto.getRemindDate());

        return Optional.of(TimeCapsuleResponseDto.toDTO(timeCapsule));
    }

    //  타임캡슐 삭제
    @Transactional
    public void deleteCapsule(Integer id) {
        if (!timeCapsuleRepository.existsById(id)) {
            throw new TuiTuiException(TuiTuiMsgCode.CAPSULE_NOT_FOUND);
        }

        timeCapsuleRepository.deleteById(id);
    }
}
