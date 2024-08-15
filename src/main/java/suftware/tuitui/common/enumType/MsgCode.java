package suftware.tuitui.common.enumType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MsgCode {
    //  200
    USER_LOGIN_SUCCESS(HttpStatus.OK, "USER-000", "로그인 성공"),
    USER_READ_SUCCESS(HttpStatus.OK, "USER-000", "유저 조회 성공"),
    USER_UPDATE_SUCCESS(HttpStatus.OK, "USER-000", "유저 업데이트 성공"),
    USER_DELETE_SUCCESS(HttpStatus.OK, "USER-000", "유저 삭제 성공"),
    PROFILE_READ_SUCCESS(HttpStatus.OK, "PROFILE-000", "프로필 조회 성공"),
    PROFILE_UPDATE_SUCCESS(HttpStatus.OK, "PROFILE-000", "프로필 업데이트 성공"),
    PROFILE_DELETE_SUCCESS(HttpStatus.OK,"PROFILE-000", "프로필 삭제 성공"),
    CAPSULE_READ_SUCCESS(HttpStatus.OK, "CAPSULE-000", "캡슐 조회 성공"),
    CAPSULE_UPDATE_SUCCESS(HttpStatus.OK, "CAPSULE-000", "캡슐 업데이트 성공"),
    CAPSULE_DELETE_SUCCESS(HttpStatus.OK, "CAPSULE-000", "캡슐 삭제 성공"),
    CAPSULE_LIKE_READ_SUCCESS(HttpStatus.OK, "CAPSULE-000", "좋아요 조회 성공"),
    CAPSULE_LIKE_DELETE_SUCCESS(HttpStatus.OK, "CAPSULE-000", "좋아요 삭제 성공"),
    CAPSULE_VISIT_READ_SUCCESS(HttpStatus.OK, "CAPSULE-000", "방문자 조회 성공"),

    IMAGE_DELETE_SUCCESS(HttpStatus.OK, "IMAGE-000", "이미지 삭제 성공"),

    COMMENT_READ_SUCCESS(HttpStatus.OK, "COMMENT-000", "댓글 조회 성공"),
    COMMENT_UPDATE_SUCCESS(HttpStatus.OK, "COMMENT-000", "댓글 업데이트 성공"),
    COMMENT_DELETE_SUCCESS(HttpStatus.OK, "COMMENT-000", "댓글 삭제 성공"),
    FOLLOWS_READ_SUCCESS(HttpStatus.OK, "FOLLOW-000", "팔로워 및 팔로잉 조회 성공"),

    //  201
    USER_SIGNUP_SUCCESS(HttpStatus.CREATED, "USER-000", "회원가입 성공"),
    PROFILE_CREATE_SUCCESS(HttpStatus.CREATED,"PROFILE-000", "프로필 생성 성공"),
    CAPSULE_CREATE_SUCCESS(HttpStatus.CREATED,"CAPSULE-000", "캡슐 생성 성공"),
    CAPSULE_LIKE_CREATE_SUCCESS(HttpStatus.CREATED, "CAPSULE-000", "좋아요 생성 성공"),
    CAPSULE_VISIT_CREATE_SUCCESS(HttpStatus.CREATED, "CAPSULE-000", "방문자 생성 성공"),
    COMMENT_CREATE_SUCCESS(HttpStatus.CREATED, "COMMENT-000", "댓글 생성 성공"),

    //  400
    SIGNUP_FAIL(HttpStatus.BAD_REQUEST,"USER-000", "회원가입 실패"),
    USER_BAD_REQUEST(HttpStatus.BAD_REQUEST, "USER-000", "계정 정보 불일치"),
    USER_EXIST(HttpStatus.BAD_REQUEST, "USER-000", "이미 생성된 계정"),
    LOGIN_FAIL(HttpStatus.BAD_REQUEST,"USER-000", "로그인 실패"),
    PROFILE_CREATE_FAIL(HttpStatus.BAD_REQUEST,"PROFILE-000", "프로필 생성 실패"),
    PROFILE_EXIT(HttpStatus.BAD_REQUEST,"PROFILE-000","이미 존재하는 프로필"),
    CAPSULE_CREATE_FAIL(HttpStatus.BAD_REQUEST,"CAPSULE-000", "캡슐 생성 실패"),
    CAPSULE_LIKE_EXIST(HttpStatus.BAD_REQUEST, "CAPSULE-000", "좋아요가 이미 존재함"),
    FILE_NOT_FOUND(HttpStatus.BAD_REQUEST, "FILE-000", "파일을 찾을 수 없음"),

    //  404
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-000", "유저를 찾을 수 없음"),
    PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND,"PROFILE-000", "존재하지 않는 프로필"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND,"COMMENT-000", "존재하지 않는 댓글"),
    CAPSULE_NOT_FOUND(HttpStatus.NOT_FOUND,"CAPSULE-000", "존재하지 않는 타임캡슐"),
    CAPSULE_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND,"CAPSULE-000", "좋아요가 존재하지 않음"),
    FOLLOWER_NOT_FOUND(HttpStatus.NOT_FOUND,"PROFILE-000", "팔로워가 존재하지 않음"),
    FOLLOWING_NOT_FOUND(HttpStatus.NOT_FOUND,"PROFILE-000", "팔로잉이 존재하지 않음"),
    FOLLOWS_NOT_FOUND(HttpStatus.NOT_FOUND,"PROFILE-000", "팔로워 및 팔로잉이 존재하지 않음");

    private final HttpStatus httpStatus;
    private final String code;
    private final String msg;
}
