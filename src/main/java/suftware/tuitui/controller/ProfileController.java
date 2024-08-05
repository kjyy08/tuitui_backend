package suftware.tuitui.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import suftware.tuitui.common.exception.CustomException;
import suftware.tuitui.common.http.Message;
import suftware.tuitui.common.enumType.MsgCode;
import suftware.tuitui.dto.request.ProfileRequestDto;
import suftware.tuitui.dto.response.ProfileResponseDto;
import suftware.tuitui.service.ProfileService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("api/")
public class ProfileController {
    private final ProfileService profileService;

    //  전체 프로필 조회
    @GetMapping(value = "profiles")
    public ResponseEntity<Message> readProfile() {
        List<ProfileResponseDto> profileResponseDtoList = profileService.getProfileList();

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(MsgCode.PROFILE_READ_SUCCESS.getMsg())
                .data(profileResponseDtoList)
                .build());
    }

    //  프로필 id 기준 조회
    @GetMapping(value = "profiles/{profileId}")
    public ResponseEntity<Message> readProfileById(@PathVariable(name = "profileId") Integer profile_id){
        Optional<ProfileResponseDto> profileResponseDto = profileService.getProfile(profile_id);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(MsgCode.PROFILE_READ_SUCCESS.getMsg())
                .data(profileResponseDto)
                .build());
    }

    //  user id 기준 조회
    @GetMapping(value = "users/{userId}/profiles")
    public ResponseEntity<Message> readProfileByUserId(@PathVariable(name = "userId") Integer profile_id) {
        Optional<ProfileResponseDto> profileResponseDto = profileService.getProfileByUserId(profile_id);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(MsgCode.PROFILE_READ_SUCCESS.getMsg())
                .data(profileResponseDto)
                .build());
    }

    //  닉네임 기준 프로필 조회
    @GetMapping(value = "profiles/nicknames/{nickname}")
    public ResponseEntity<Message> readProfileByNickname(@PathVariable(name = "nickname") String nickname) {
        Optional<ProfileResponseDto> profileResponseDto = profileService.getProfileByNickname(nickname);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(MsgCode.PROFILE_READ_SUCCESS.getMsg())
                .data(profileResponseDto)
                .build());
    }

    //  프로필 생성, 이미지 미포함
    @PostMapping(value = "profiles/without-image")
    public ResponseEntity<Message> createProfileJson(@RequestBody ProfileRequestDto profileRequestDto) {
        //  프로필 생성
        Optional<ProfileResponseDto> profileResponseDto = profileService.saveProfile(profileRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(Message.builder()
                .status(HttpStatus.CREATED)
                .message(MsgCode.PROFILE_CREATE_SUCCESS.getMsg())
                .data(profileResponseDto)
                .build());
    }

    //  프로필 생성, 이미지 포함
    @PostMapping(value = "profiles/with-image", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Message> createProfileMultipart(@RequestPart(name = "request") ProfileRequestDto profileRequestDto,
                                                        @RequestPart(name = "file", required = false) MultipartFile file) throws IOException {
        Optional<ProfileResponseDto> profileResponseDto = profileService.saveProfile(profileRequestDto, file);

        return ResponseEntity.status(HttpStatus.CREATED).body(Message.builder()
                .status(HttpStatus.CREATED)
                .message(MsgCode.PROFILE_CREATE_SUCCESS.getMsg())
                .data(profileResponseDto)
                .build());
    }

    //  프로필 수정, 이미지 미포함
    @PutMapping(value = "profiles")
    public ResponseEntity<Message> updateProfile(@RequestBody ProfileRequestDto profileRequestDto) {
        Optional<ProfileResponseDto> profileResponseDto = profileService.updateProfile(profileRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(MsgCode.PROFILE_UPDATE_SUCCESS.getMsg())
                .data(profileResponseDto)
                .build());
    }

    //  프로필 삭제, 이미지 미포함
    @DeleteMapping(value = "profiles/{profileId}")
    public ResponseEntity<Message> deleteProfile(@PathVariable(name = "profileId") Integer profileId){
        profileService.deleteProfile(profileId);

        return ResponseEntity.status(HttpStatus.OK).body(Message.builder()
                .status(HttpStatus.OK)
                .message(MsgCode.PROFILE_DELETE_SUCCESS.getMsg())
                .build());
    }
}
