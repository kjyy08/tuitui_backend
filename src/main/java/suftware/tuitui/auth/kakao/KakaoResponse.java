package suftware.tuitui.auth.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class KakaoResponse {
    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    public class KakaoAccount{
        String email;

        //  24.09.01
        //  앱 자체 프로필 생성 때 필요하기에 이메일만 받아서 검증
        //  String name;
        //  String birthyear;
        //  String birthday;
        //  String gender;
        //  String phone_number;
    }
}
