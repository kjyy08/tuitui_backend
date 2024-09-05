package suftware.tuitui.sns.naver;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class NaverResponse {
    @JsonProperty("resultcode")
    String resultCode;

    @JsonProperty("message")
    String message;

    @JsonProperty("response")
    private NaverAccount naverAccount;

    @Getter
    public class NaverAccount{
        String email;

        //  String nickname;
        //  String name;
        //  String gender;
        //  String age;
        //  String birthday;
        //  String birthyear;
    }
}
