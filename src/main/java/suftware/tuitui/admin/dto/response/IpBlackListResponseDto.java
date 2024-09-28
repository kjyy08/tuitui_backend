package suftware.tuitui.admin.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import suftware.tuitui.domain.IpBlackList;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IpBlackListResponseDto {
    Integer ipId;
    String address;
    Boolean banned;

    public static IpBlackListResponseDto toDto(IpBlackList ipBlackList){
        return IpBlackListResponseDto.builder()
                .ipId(ipBlackList.getIpId())
                .address(ipBlackList.getIpAddress())
                .banned(ipBlackList.getIsBanned())
                .build();
    }
}
