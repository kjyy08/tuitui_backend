package suftware.tuitui.controller;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import suftware.tuitui.common.exception.CustomException;
import suftware.tuitui.common.http.Message;
import suftware.tuitui.common.enumType.TuiTuiMsgCode;
import suftware.tuitui.common.valid.ProfileValidationGroups;
import suftware.tuitui.dto.request.ProfileRequestDto;
import suftware.tuitui.dto.response.ProfileResponseDto;
import suftware.tuitui.service.ProfileService;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("api/")
@Slf4j
public class ProfileController {
    private final ProfileService profileService;

    private HashMap<String, String> getValidatorResult(BindingResult bindingResult) {
        HashMap<String, String> validatorResult = new HashMap<>();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            validatorResult.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return validatorResult;
    }

    //  전체 프로필 조회
    @GetMapping(value = "profiles")
    public ResponseEntity<Message> readProfile() {
        List<ProfileResponseDto> profileResponseDtoList = profileService.getProfileList();

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(TuiTuiMsgCode.PROFILE_READ_SUCCESS.getMsg())
                .data(profileResponseDtoList)
                .build());
    }

    //  프로필 id 기준 조회
    @GetMapping(value = "profiles/{profileId}")
    public ResponseEntity<Message> readProfileById(@PathVariable(name = "profileId") Integer profile_id){
        Optional<ProfileResponseDto> profileResponseDto = profileService.getProfile(profile_id);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(TuiTuiMsgCode.PROFILE_READ_SUCCESS.getMsg())
                .data(profileResponseDto)
                .build());
    }

    //  user id 기준 조회
    @GetMapping(value = "users/{userId}/profiles")
    public ResponseEntity<Message> readProfileByUserId(@PathVariable(name = "userId") Integer profile_id) {
        Optional<ProfileResponseDto> profileResponseDto = profileService.getProfileByUserId(profile_id);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(TuiTuiMsgCode.PROFILE_READ_SUCCESS.getMsg())
                .data(profileResponseDto)
                .build());
    }

    //  닉네임 기준 프로필 조회
    @GetMapping(value = "profiles/nicknames/{nickname}")
    public ResponseEntity<Message> readProfileByNickname(@PathVariable(name = "nickname") String nickname) {
        Optional<ProfileResponseDto> profileResponseDto = profileService.getProfileByNickname(nickname);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(TuiTuiMsgCode.PROFILE_READ_SUCCESS.getMsg())
                .data(profileResponseDto)
                .build());
    }

    //  프로필 생성, 이미지 미포함
    @PostMapping(value = "profiles/without-image")
    public ResponseEntity<Message> createProfileJson(@RequestBody @Validated({ProfileValidationGroups.modify.class, ProfileValidationGroups.request.class}) ProfileRequestDto profileRequestDto,
                                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            throw new CustomException(TuiTuiMsgCode.PROFILE_CREATE_FAIL, getValidatorResult(bindingResult));
        }

        //  프로필 생성
        Optional<ProfileResponseDto> profileResponseDto = profileService.saveProfile(profileRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(Message.builder()
                .status(HttpStatus.CREATED)
                .message(TuiTuiMsgCode.PROFILE_CREATE_SUCCESS.getMsg())
                .data(profileResponseDto)
                .build());
    }

    //  프로필 생성, 이미지 포함
    @PostMapping(value = "profiles/with-image", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Message> createProfileMultipart(@RequestPart(name = "request") @Valid ProfileRequestDto profileRequestDto,
                                                        @RequestPart(name = "file", required = false) MultipartFile file, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            throw new CustomException(TuiTuiMsgCode.PROFILE_CREATE_FAIL, getValidatorResult(bindingResult));
        }

        Optional<ProfileResponseDto> profileResponseDto = profileService.saveProfile(profileRequestDto, file);

        return ResponseEntity.status(HttpStatus.CREATED).body(Message.builder()
                .status(HttpStatus.CREATED)
                .message(TuiTuiMsgCode.PROFILE_CREATE_SUCCESS.getMsg())
                .data(profileResponseDto)
                .build());
    }

    //  프로필 수정, 이미지 미포함
    @PutMapping(value = "profiles")
    public ResponseEntity<Message> updateProfile(@RequestBody @Validated(ProfileValidationGroups.request.class) ProfileRequestDto profileRequestDto,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            throw new CustomException(TuiTuiMsgCode.PROFILE_NOT_VALID, getValidatorResult(bindingResult));
        }

        Optional<ProfileResponseDto> profileResponseDto = profileService.updateProfile(profileRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(TuiTuiMsgCode.PROFILE_UPDATE_SUCCESS.getMsg())
                .data(profileResponseDto)
                .build());
    }

    //  프로필 삭제
    @DeleteMapping(value = "profiles/{profileId}")
    public ResponseEntity<Message> deleteProfile(@PathVariable(name = "profileId") Integer profileId){
        profileService.deleteProfile(profileId);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(TuiTuiMsgCode.PROFILE_DELETE_SUCCESS.getMsg())
                .build());
    }
}
