package suftware.tuitui.config.http;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum MsgCode {
    USER_NOT_FOUND(0, "존재하지 않는 계정"),
    USER_EXIST(1, "이미 생성된 계정"),

    LOGIN_SUCCESS(2, "로그인 성공"),
    LOGIN_FAIL(3, "로그인 실패"),

    SIGNUP_SUCCESS(4, "회원가입 성공"),
    SIGNUP_FAIL(5, "회원가입 실패"),

    PROFILE_CREATE_SUCCESS(100, "프로필 생성 성공"),
    PROFILE_CREATE_FAIL(101, "프로필 생성 실패"),
    PROFILE_EXIT(102 ,"이미 존재하는 프로필"),
    PROFILE_NOT_FOUND(103, "존재하지 않는 프로필"),

    CAPSULE_CREATE_SUCCESS(100, "캡슐 생성 성공"),
    CAPSULE_CREATE_FAIL(201, "캡슐 생성 실패"),
    CAPSULE_NOT_FOUND(203, "존재하지 않는 타임캡슐"),

    COMMENT_NOT_FOUND(9, "존재하지 않는 댓글"),
    CAPSULE_LIKE_EMPTY(10, "캡슐 좋아요의 수가 없음"),

    CREATE_SUCCESS(1000, "데이터 생성 성공"),
    READ_SUCCESS(1001, "데이터 조회 성공"),
    UPDATE_SUCCESS(1002, "데이터 갱신 성공"),
    DELETE_SUCCESS(1003, "데이터 삭제 성공"),

    CREATE_FAIL(2000, "데이터 생성 실패"),
    READ_FAIL(2001, "데이터 조회 실패"),
    UPDATE_FAIL(2002, "데이터 갱신 실패"),
    DELETE_FAIL(2003, "데이터 삭제 실패");

    private final Integer code;

    @JsonValue
    private final String msg;

    MsgCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
