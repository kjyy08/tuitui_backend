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

        TimeCapsuleVisit timeCapsuleVisit = timeCapsuleVisitRepository.save(TimeCapsuleVisit.of(timeCapsule));

        return Optional.of(TimeCapsuleVisitResponseDto.toDTO(timeCapsuleVisit));
    }

    @Transactional
    public Optional<TimeCapsuleVisitResponseDto> addCapsuleVisitCount(Integer capsuleId) {
        TimeCapsule timeCapsule = timeCapsuleRepository.findById(capsuleId)
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.CAPSULE_NOT_FOUND));

        Optional<TimeCapsuleVisit> timeCapsuleVisit = timeCapsuleVisitRepository.findByTimeCapsule_TimeCapsuleId(capsuleId);

        if (timeCapsuleVisit.isEmpty()){
            timeCapsuleVisit = Optional.of(timeCapsuleVisitRepository.save(TimeCapsuleVisit.of(timeCapsule)));
        }

        timeCapsuleVisit.get().addVisitCount();

        return Optional.of(TimeCapsuleVisitResponseDto.toDTO(timeCapsuleVisit.get()));
    }
}
