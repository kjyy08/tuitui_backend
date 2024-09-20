package suftware.tuitui.common.enumType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TuiTuiMsgCode {
    // 200 OK responses
    USER_LOGIN_SUCCESS(HttpStatus.OK, "USER-001", "로그인 성공"),
    USER_LOGOUT_SUCCESS(HttpStatus.OK, "USER-002", "로그아웃 성공"),
    USER_READ_SUCCESS(HttpStatus.OK, "USER-003", "유저 조회 성공"),
    USER_UPDATE_SUCCESS(HttpStatus.OK, "USER-004", "유저 업데이트 성공"),
    USER_DELETE_SUCCESS(HttpStatus.OK, "USER-005", "유저 삭제 성공"),

    PROFILE_READ_SUCCESS(HttpStatus.OK, "PROFILE-001", "프로필 조회 성공"),
    PROFILE_UPDATE_SUCCESS(HttpStatus.OK, "PROFILE-002", "프로필 업데이트 성공"),
    PROFILE_DELETE_SUCCESS(HttpStatus.OK, "PROFILE-003", "프로필 삭제 성공"),

    CAPSULE_READ_SUCCESS(HttpStatus.OK, "CAPSULE-001", "캡슐 조회 성공"),
    CAPSULE_UPDATE_SUCCESS(HttpStatus.OK, "CAPSULE-002", "캡슐 업데이트 성공"),
    CAPSULE_DELETE_SUCCESS(HttpStatus.OK, "CAPSULE-003", "캡슐 삭제 성공"),
    CAPSULE_LIKE_READ_SUCCESS(HttpStatus.OK, "CAPSULE-004", "캡슐 좋아요 조회 성공"),
    CAPSULE_LIKE_DELETE_SUCCESS(HttpStatus.OK, "CAPSULE-005", "캡슐 좋아요 삭제 성공"),
    CAPSULE_VISIT_READ_SUCCESS(HttpStatus.OK, "CAPSULE-006", "캡슐 조회수 조회 성공"),

    COMMENT_READ_SUCCESS(HttpStatus.OK, "COMMENT-001", "댓글 조회 성공"),
    COMMENT_UPDATE_SUCCESS(HttpStatus.OK, "COMMENT-002", "댓글 업데이트 성공"),
    COMMENT_DELETE_SUCCESS(HttpStatus.OK, "COMMENT-003", "댓글 삭제 성공"),

    FOLLOWS_READ_SUCCESS(HttpStatus.OK, "FOLLOW-001", "팔로워 및 팔로잉 조회 성공"),
    FOLLOWS_DELETE_SUCCESS(HttpStatus.OK, "FOLLOW-002", "팔로우 삭제 성공"),

    IMAGES_DELETE_SUCCESS(HttpStatus.OK, "IMAGE-001", "이미지 삭제 성공"),

    COMMENT_LIKE_DELETE_SUCCESS(HttpStatus.OK, "COMMENT-004", "좋아요 삭제 성공"),
    COMMENT_LIKE_READ_SUCCESS(HttpStatus.OK, "COMMENT-005", "좋아요 조회 성공"),

    // 201 Created responses
    USER_SIGNUP_SUCCESS(HttpStatus.CREATED, "USER-006", "회원가입 성공"),

    PROFILE_CREATE_SUCCESS(HttpStatus.CREATED, "PROFILE-004", "프로필 생성 성공"),

    CAPSULE_CREATE_SUCCESS(HttpStatus.CREATED, "CAPSULE-007", "캡슐 생성 성공"),
    CAPSULE_LIKE_CREATE_SUCCESS(HttpStatus.CREATED, "CAPSULE-008", "좋아요 생성 성공"),
    CAPSULE_VISIT_CREATE_SUCCESS(HttpStatus.CREATED, "CAPSULE-009", "조회수 증가 성공"),

    COMMENT_CREATE_SUCCESS(HttpStatus.CREATED, "COMMENT-006", "댓글 생성 성공"),
    COMMENT_LIKE_CREATE_SUCCESS(HttpStatus.CREATED, "COMMENT-007", "좋아요 생성 성공"),

    FOLLOWS_CREATE_SUCCESS(HttpStatus.CREATED, "FOLLOW-003", "팔로우 생성 성공"),

    IMAGES_CREATE_SUCCESS(HttpStatus.CREATED, "IMAGE-002", "이미지 업로드 성공"),

    // 400 Bad Request responses
    USER_SIGNUP_FAIL(HttpStatus.BAD_REQUEST, "USER-007", "회원가입 실패"),
    USER_LOGOUT_FAIL(HttpStatus.BAD_REQUEST, "USER-008", "로그아웃 실패"),
    USER_SIGNUP_FAIL_NOT_ENCODED(HttpStatus.BAD_REQUEST, "USER-009", "암호화 실패"),
    USER_BAD_REQUEST(HttpStatus.BAD_REQUEST, "USER-010", "유저 정보 불일치"),
    USER_NOT_VALID(HttpStatus.BAD_REQUEST, "USER-011", "유효하지 않은 정보"),
    USER_EXIST(HttpStatus.BAD_REQUEST, "USER-012", "해당 이메일 주소로 가입된 다른 계정이 존재함"),
    USER_LOGIN_FAIL(HttpStatus.BAD_REQUEST, "USER-013", "로그인 실패"),
    USER_LOGIN_FAIL_EXIST(HttpStatus.BAD_REQUEST, "USER-014", "이미 로그인된 유저"),

    PROFILE_CREATE_FAIL(HttpStatus.BAD_REQUEST, "PROFILE-005", "프로필 생성 실패"),
    PROFILE_NOT_VALID(HttpStatus.BAD_REQUEST, "PROFILE-006", "유효하지 않은 정보"),
    PROFILE_EXIST(HttpStatus.BAD_REQUEST, "PROFILE-007", "이미 존재하는 프로필"),
    PROFILE_EXIST_PHONE(HttpStatus.BAD_REQUEST, "PROFILE-008", "이미 가입된 전화번호"),
    PROFILE_EXIST_NICKNAME(HttpStatus.BAD_REQUEST, "PROFILE-009", "닉네임 중복"),
    PROFILE_UPDATE_FAIL(HttpStatus.BAD_REQUEST, "PROFILE-011", "프로필 업데이트 실패"),

    CAPSULE_CREATE_FAIL(HttpStatus.BAD_REQUEST, "CAPSULE-010", "캡슐 생성 실패"),
    CAPSULE_LIKE_EXIST(HttpStatus.BAD_REQUEST, "CAPSULE-011", "좋아요가 이미 존재함"),

    COMMENT_CREATE_FAIL(HttpStatus.BAD_REQUEST, "COMMENT-011", "댓글 생성 실패"),
    COMMENT_UPDATE_FAIL(HttpStatus.BAD_REQUEST, "COMMENT-012", "댓글 수정 실패"),
    COMMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "COMMENT-013", "유효하지 않은 댓글"),
    COMMENT_LIKE_EXIST(HttpStatus.BAD_REQUEST, "COMMENT-008", "좋아요가 이미 존재함"),

    FOLLOWS_EXIST(HttpStatus.BAD_REQUEST, "FOLLOW-004", "이미 팔로우 중인 유저"),
    FOLLOWS_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "FOLLOW-008", "자기 자신을 팔로우 할 수 없음"),

    IMAGE_CREATE_FAIL(HttpStatus.BAD_REQUEST, "IMAGE-003", "이미지 생성 실패"),
    IMAGE_S3_UPLOAD_FAIL(HttpStatus.BAD_REQUEST, "IMAGE-004", "S3 이미지 업로드 실패"),
    IMAGE_S3_DELETE_FAIL(HttpStatus.BAD_REQUEST, "IMAGE-006", "S3 이미지 삭제 실패"),

    SNS_AUTH_BAD_REQUEST(HttpStatus.BAD_REQUEST, "SNS-001", "소셜 로그인 정보 불일치"),

    //  403 Forbidden responses
    IP_BANNED(HttpStatus.FORBIDDEN, "SERVER-002", "비정상적인 접근"),

    // 404 Not Found responses
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-015", "유저를 찾을 수 없음"),

    PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "PROFILE-010", "프로필을 찾을 수 없음"),

    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT-009", "댓글을 찾을 수 없음"),
    COMMENT_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT-010", "댓글 좋아요가 존재하지 않음"),

    CAPSULE_NOT_FOUND(HttpStatus.NOT_FOUND, "CAPSULE-012", "타임캡슐을 찾을 수 없음"),
    CAPSULE_LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "CAPSULE-013", "캡슐 좋아요가 존재하지 않음"),

    FOLLOWER_NOT_FOUND(HttpStatus.NOT_FOUND, "FOLLOW-005", "팔로워를 찾을 수 없음"),
    FOLLOWING_NOT_FOUND(HttpStatus.NOT_FOUND, "FOLLOW-006", "팔로잉이 존재하지 않음"),
    FOLLOWS_NOT_FOUND(HttpStatus.NOT_FOUND, "FOLLOW-007", "팔로워 및 팔로잉이 존재하지 않음"),

    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "FILE-001", "파일을 찾을 수 없음"),

    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "IMAGE-005", "이미지를 찾을 수 없음"),

    //  500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER-001", "서버에서 요청에 대한 처리를 할 수 없음");

    private final HttpStatus httpStatus;
    private final String code;
    private final String msg;
}
