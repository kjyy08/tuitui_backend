package suftware.tuitui.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FollowResponseDto {
    List<FollowDto> followerList;
    List<FollowDto> followingList;

    public static FollowResponseDto toDto(List<FollowDto> followerList, List<FollowDto> followingList){
        return FollowResponseDto.builder()
                .followerList(followerList)
                .followingList(followingList)
                .build();
    }
}
