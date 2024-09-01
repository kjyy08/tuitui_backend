package suftware.tuitui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.common.exception.TuiTuiException;
import suftware.tuitui.domain.TimeCapsuleVisit;
import suftware.tuitui.dto.response.TimeCapsuleVisitResponseDto;
import suftware.tuitui.repository.TimeCapsuleVisitRepository;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimeCapsuleVisitService {
    private final TimeCapsuleVisitRepository timeCapsuleVisitRepository;

    public Optional<TimeCapsuleVisitResponseDto> getCapsuleVisitCount(Integer id) {
        TimeCapsuleVisit timeCapsuleVisit = timeCapsuleVisitRepository.findByTimeCapsule_TimeCapsuleId(id)
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.CAPSULE_NOT_FOUND));

        return Optional.of(TimeCapsuleVisitResponseDto.toDTO(timeCapsuleVisit));
    }

    @Transactional
    public Optional<TimeCapsuleVisitResponseDto> addCapsuleVisitCount(Integer id) {
        TimeCapsuleVisit timeCapsuleVisit = timeCapsuleVisitRepository.findByTimeCapsule_TimeCapsuleId(id)
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.CAPSULE_NOT_FOUND));

        timeCapsuleVisit.setVisitCount(timeCapsuleVisit.getVisitCount() + 1);
        return Optional.of(TimeCapsuleVisitResponseDto.toDTO(timeCapsuleVisit));
    }

}
