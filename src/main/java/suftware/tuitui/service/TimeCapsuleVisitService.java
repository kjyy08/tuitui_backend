package suftware.tuitui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.common.exception.TuiTuiException;
import suftware.tuitui.domain.TimeCapsule;
import suftware.tuitui.domain.TimeCapsuleVisit;
import suftware.tuitui.dto.response.TimeCapsuleVisitResponseDto;
import suftware.tuitui.repository.TimeCapsuleRepository;
import suftware.tuitui.repository.TimeCapsuleVisitRepository;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimeCapsuleVisitService {
    private final TimeCapsuleVisitRepository timeCapsuleVisitRepository;
    private final TimeCapsuleRepository timeCapsuleRepository;

    public Optional<TimeCapsuleVisitResponseDto> getCapsuleVisitCount(Integer capsuleId) {
        TimeCapsuleVisit timeCapsuleVisit = timeCapsuleVisitRepository.findByTimeCapsule_TimeCapsuleId(capsuleId)
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.CAPSULE_NOT_FOUND));

        return Optional.of(TimeCapsuleVisitResponseDto.toDTO(timeCapsuleVisit));
    }

    public Optional<TimeCapsuleVisitResponseDto> createCapsuleVisitCount(Integer capsuleId){
        TimeCapsule timeCapsule = timeCapsuleRepository.findById(capsuleId)
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.CAPSULE_NOT_FOUND));

        TimeCapsuleVisit timeCapsuleVisit = timeCapsuleVisitRepository.save(TimeCapsuleVisit.builder()
                .timeCapsule(timeCapsule)
                .visitCount(0)
                .build());

        return Optional.of(TimeCapsuleVisitResponseDto.toDTO(timeCapsuleVisit));
    }

    @Transactional
    public Optional<TimeCapsuleVisitResponseDto> addCapsuleVisitCount(Integer capsuleId) {
        TimeCapsule timeCapsule = timeCapsuleRepository.findById(capsuleId)
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.CAPSULE_NOT_FOUND));

        TimeCapsuleVisit timeCapsuleVisit;

        if (!timeCapsuleVisitRepository.existsByTimeCapsule_TimeCapsuleId(capsuleId)){
            //  타임캡슐은 있으나 캡슐 조회 도메인이 존재하지 않는 경우 초기화 해서 생성
            timeCapsuleVisit = timeCapsuleVisitRepository.save(TimeCapsuleVisit.builder()
                    .timeCapsule(timeCapsule)
                    .visitCount(0)
                    .build());
        } else {
            //  기존 캡슐 조회 도메인 조회
            timeCapsuleVisit = timeCapsuleVisitRepository.findByTimeCapsule_TimeCapsuleId(capsuleId)
                    .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.CAPSULE_NOT_FOUND));
        }

        timeCapsuleVisit.setVisitCount(timeCapsuleVisit.getVisitCount() + 1);
        return Optional.of(TimeCapsuleVisitResponseDto.toDTO(timeCapsuleVisit));
    }
}
