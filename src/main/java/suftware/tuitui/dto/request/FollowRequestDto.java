package suftware.tuitui.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import suftware.tuitui.domain.Follow;
import suftware.tuitui.domain.Profile;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FollowRequestDto {
    Integer followerId;
    Integer followingId;

    public static Follow toEntity(Profile follower, Profile following){
        return Follow.of(follower, following);
    }
}
