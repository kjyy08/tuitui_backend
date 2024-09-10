package suftware.tuitui.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimeCapsulePageResponse {
    private List<TimeCapsuleResponseDto> contents;
    private Integer pageNo;
    private Integer pageSize;
    private Integer totalElements;
    private Integer totalPages;
    private Boolean lastPage;
}
