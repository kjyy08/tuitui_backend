package suftware.tuitui.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import suftware.tuitui.domain.TimeCapsuleVisit;
import suftware.tuitui.dto.response.TimeCapsuleResponseDto;
import suftware.tuitui.dto.response.TimeCapsuleVisitResponseDto;
import suftware.tuitui.repository.TimeCapsuleVisitRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimeCapsuleVisitService {
    private final TimeCapsuleVisitRepository timeCapsuleVisitRepository;

    public Optional<TimeCapsuleVisitResponseDto> getCapsuleVisitCount(Integer id){
        Optional<TimeCapsuleVisit> timeCapsuleVisit = timeCapsuleVisitRepository.findByTimeCapsule_TimeCapsuleId(id);

        if (timeCapsuleVisit.isEmpty()){
            return Optional.empty();
        }
        else {
            return Optional.of(TimeCapsuleVisitResponseDto.toDTO(timeCapsuleVisit.get()));
        }
    }

    public Optional<TimeCapsuleVisitResponseDto> addCapsuleVisitCount(Integer id){
        Optional<TimeCapsuleVisit> timeCapsuleVisit = timeCapsuleVisitRepository.findByTimeCapsule_TimeCapsuleId(id);

        if (timeCapsuleVisit.isEmpty()){
            return Optional.empty();
        }
        else {
            timeCapsuleVisit.get().setVisitCount(timeCapsuleVisit.get().getVisitCount() + 1);
            return Optional.of(TimeCapsuleVisitResponseDto.toDTO(timeCapsuleVisit.get()));
        }
    }

}
