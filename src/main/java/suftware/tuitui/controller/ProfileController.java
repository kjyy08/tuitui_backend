package suftware.tuitui.controller;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import suftware.tuitui.common.enumType.S3ImagePath;
import suftware.tuitui.common.exception.TuiTuiException;
import suftware.tuitui.common.http.HttpResponse;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.dto.request.ProfileCreateRequestDto;
import suftware.tuitui.dto.request.ProfileUpdateRequestDto;
import suftware.tuitui.dto.response.ImageResponseDto;
import suftware.tuitui.dto.response.ProfileResponseDto;
import suftware.tuitui.service.ProfileImageService;
import suftware.tuitui.service.ProfileService;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("api/")
@Slf4j
public class ProfileController {
    private final ProfileService profileService;
    private final ProfileImageService profileImageService;

    //  전체 프로필 조회
    @GetMapping(value = "profiles")
    public ResponseEntity<HttpResponse> readProfile() {
        List<ProfileResponseDto> profileResponseDtoList = profileService.getProfileList();

        for (ProfileResponseDto profileResponseDto : profileResponseDtoList){
            ImageResponseDto imageResponseDto = profileImageService.readProfileImage(profileResponseDto.getProfileId())
                    .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.IMAGE_NOT_FOUND));

            profileResponseDto.setProfileImgPath(imageResponseDto.getImagePath());
        }

        return HttpResponse.toResponseEntity(TuiTuiMsgCode.PROFILE_READ_SUCCESS, profileResponseDtoList);
    }

    //  프로필 id 기준 조회
    @GetMapping(value = "profiles/{profileId}")
    public ResponseEntity<HttpResponse> readProfileById(@PathVariable(name = "profileId") Integer profileId){
        ProfileResponseDto profileResponseDto = profileService.getProfile(profileId)
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.PROFILE_NOT_FOUND));
        ImageResponseDto imageResponseDto = profileImageService.readProfileImage(profileResponseDto.getProfileId())
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.IMAGE_NOT_FOUND));

        profileResponseDto.setProfileImgPath(imageResponseDto.getImagePath());

        return HttpResponse.toResponseEntity(TuiTuiMsgCode.PROFILE_READ_SUCCESS, profileResponseDto);
    }

    //  user id 기준 조회
    @GetMapping(value = "users/{userId}/profiles")
    public ResponseEntity<HttpResponse> readProfileByUserId(@PathVariable(name = "userId") Integer userId) {
        ProfileResponseDto profileResponseDto = profileService.getProfileByUserId(userId)
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.PROFILE_NOT_FOUND));
        ImageResponseDto imageResponseDto = profileImageService.readProfileImage(profileResponseDto.getProfileId())
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.IMAGE_NOT_FOUND));

        profileResponseDto.setProfileImgPath(imageResponseDto.getImagePath());

        return HttpResponse.toResponseEntity(TuiTuiMsgCode.PROFILE_READ_SUCCESS, profileResponseDto);
    }

    //  닉네임 기준 프로필 조회
    @GetMapping(value = "profiles/nicknames/{nickname}")
    public ResponseEntity<HttpResponse> readProfileByNickname(@PathVariable(name = "nickname") String nickname) {
        ProfileResponseDto profileResponseDto = profileService.getProfileByNickname(nickname)
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.PROFILE_NOT_FOUND));
        ImageResponseDto imageResponseDto = profileImageService.readProfileImage(profileResponseDto.getProfileId())
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.IMAGE_NOT_FOUND));

        profileResponseDto.setProfileImgPath(imageResponseDto.getImagePath());

        return HttpResponse.toResponseEntity(TuiTuiMsgCode.PROFILE_READ_SUCCESS, profileResponseDto);
    }

    //  프로필 생성, 이미지 미포함
    @PostMapping(value = "profiles/without-image")
    public ResponseEntity<HttpResponse> createProfileWithJson(@Valid @RequestBody ProfileCreateRequestDto profileCreateRequestDto,
                                                              Authentication authentication) throws MethodArgumentNotValidException {
        //  프로필 생성
        ProfileResponseDto profileResponseDto = profileService.saveProfile(profileCreateRequestDto)
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.PROFILE_CREATE_FAIL));

        ImageResponseDto imageResponseDto = profileImageService.createProfileBasicImage(profileResponseDto.getProfileId())
                .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.PROFILE_CREATE_FAIL));

        profileResponseDto.setProfileImgPath(imageResponseDto.getImagePath());

        return HttpResponse.toResponseEntity(TuiTuiMsgCode.PROFILE_CREATE_SUCCESS, profileResponseDto);
    }

    //  프로필 생성, 이미지 포함
    @PostMapping(value = "profiles/with-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpResponse> createProfileWithImage(@Valid @RequestPart(name = "request") ProfileCreateRequestDto profileCreateRequestDto,
                                                               @RequestPart(name = "file", required = true) MultipartFile file) throws MethodArgumentNotValidException {
        //  파일이 존재하는 경우에만 동작
        if(file != null && !file.isEmpty()) {
            ProfileResponseDto profileResponseDto = profileService.saveProfile(profileCreateRequestDto)
                    .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.PROFILE_CREATE_FAIL));

            ImageResponseDto imageResponseDto = profileImageService.uploadProfileImage(profileResponseDto.getProfileId(), S3ImagePath.PROFILE.getPath(), file)
                    .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.IMAGE_S3_UPLOAD_FAIL));

            profileResponseDto.setProfileImgPath(imageResponseDto.getImagePath());

            return HttpResponse.toResponseEntity(TuiTuiMsgCode.PROFILE_CREATE_SUCCESS, profileResponseDto);
        } else {
            return HttpResponse.toResponseEntity(TuiTuiMsgCode.PROFILE_CREATE_FAIL);
        }
    }

    //  프로필 이미지 수정
    @PostMapping(value = "profiles/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HttpResponse> updateProfileImage(@RequestPart(name = "profileId") Integer profileId,
                                                           @RequestPart(name = "file", required = false) MultipartFile file){
        ImageResponseDto imageResponseDto;

        //  파일이 비어있지 않으면 해당 이미지로 변경, 그렇지 않으면 기본 프로필 이미지로 변경
        if(file != null && !file.isEmpty()) {
            imageResponseDto = profileImageService.updateProfileImage(profileId, S3ImagePath.PROFILE.getPath(), file)
                    .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.PROFILE_UPDATE_FAIL));
        } else {
            //  기본 프로필 이미지로 변경
            imageResponseDto = profileImageService.deleteProfileImage(profileId, S3ImagePath.PROFILE.getPath())
                    .orElseThrow(() -> new TuiTuiException(TuiTuiMsgCode.PROFILE_UPDATE_FAIL));
        }

        return HttpResponse.toResponseEntity(TuiTuiMsgCode.PROFILE_UPDATE_SUCCESS, imageResponseDto);
    }

    //  프로필 정보 수정
    @PutMapping(value = "profiles")
    public ResponseEntity<HttpResponse> updateProfile(@Valid @RequestBody ProfileUpdateRequestDto profileUpdateRequestDto) throws MethodArgumentNotValidException{
        Optional<ProfileResponseDto> profileResponseDto = profileService.updateProfile(profileUpdateRequestDto);

        return HttpResponse.toResponseEntity(TuiTuiMsgCode.PROFILE_UPDATE_SUCCESS, profileResponseDto);
    }

    //  프로필 삭제
    @DeleteMapping(value = "profiles/{profileId}")
    public ResponseEntity<HttpResponse> deleteProfile(@PathVariable(name = "profileId") Integer profileId){
        profileImageService.deleteProfileImage(profileId, S3ImagePath.PROFILE.getPath());
        profileService.deleteProfile(profileId);

        return HttpResponse.toResponseEntity(TuiTuiMsgCode.PROFILE_DELETE_SUCCESS);
    }
}
