package suftware.tuitui.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import suftware.tuitui.admin.dto.response.IpBlackListResponseDto;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.common.exception.TuiTuiException;
import suftware.tuitui.domain.IpBlackList;
import suftware.tuitui.dto.response.PageResponse;
import suftware.tuitui.repository.IpBlackListRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class IpBlackListService {
    private final IpBlackListRepository ipBlackListRepository;

    public Optional<PageResponse> readIpBlackList(Integer pageNo, Integer pageSize, String sortBy){
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        Page<IpBlackList> ipBlackListPage = ipBlackListRepository.findAll(pageable);

        List<IpBlackListResponseDto> ipBlackListResponseDtoList = ipBlackListPage.getContent().stream()
                .map(IpBlackListResponseDto::toDto)
                .collect(Collectors.toList());

        // PageResponse 반환
        return Optional.of(PageResponse.builder()
                .contents(ipBlackListResponseDtoList)
                .pageNo(ipBlackListPage.getNumber())
                .pageSize(ipBlackListPage.getSize())
                .totalElements((int) ipBlackListPage.getTotalElements())
                .totalPages(ipBlackListPage.getTotalPages())
                .lastPage(ipBlackListPage.isLast())
                .build());
    }

    public void deleteIp(Integer id){
        if (!ipBlackListRepository.existsById(id)){
            throw new TuiTuiException(TuiTuiMsgCode.USER_NOT_FOUND);
        }

        ipBlackListRepository.deleteById(id);
    }
}
