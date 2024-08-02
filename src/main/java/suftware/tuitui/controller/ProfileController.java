package suftware.tuitui.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import suftware.tuitui.config.http.Message;
import suftware.tuitui.config.http.MsgCode;
import suftware.tuitui.domain.Profile;
import suftware.tuitui.dto.request.ProfileRequestDto;
import suftware.tuitui.dto.response.ProfileResponseDto;
import suftware.tuitui.service.ProfileService;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("api/")
public class ProfileController {
    private final ProfileService profileService;

    //  전체 프로필 조회
    @GetMapping(value = "profiles")
    public ResponseEntity<Message> getProfile(){
        Message message = new Message();
        List<ProfileResponseDto> profileResponseDtoList =  profileService.getProfileList();

        if (profileResponseDtoList.isEmpty()){
            message.setStatus(HttpStatus.NOT_FOUND.value());
            message.setMessage(MsgCode.PROFILE_NOT_FOUND.getMsg());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(message);
        }
        else {
            message.setStatus(HttpStatus.OK.value());
            message.setMessage(MsgCode.READ_SUCCESS.getMsg());
            message.setData(profileResponseDtoList);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(message);
        }
    }

    //  프로필 id 기준 조회
    @GetMapping(value = "profiles/{profileId}")
    public ResponseEntity<Message> getProfileById(@PathVariable(name = "profileId") Integer profile_id){
        Message message = new Message();
        Optional<ProfileResponseDto> profileResponseDto = profileService.getProfile(profile_id);

        if (profileResponseDto.isEmpty()){
            message.setStatus(HttpStatus.NOT_FOUND.value());
            message.setMessage(MsgCode.PROFILE_NOT_FOUND.getMsg());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(message);
        }
        else{
            message.setStatus(HttpStatus.OK.value());
            message.setMessage(MsgCode.READ_SUCCESS.getMsg());
            message.setData(profileResponseDto);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(message);
        }
    }

    //  user id 기준 조회
    @GetMapping(value = "users/{userId}/profiles")
    public ResponseEntity<Message> getProfileByUserId(@PathVariable(name = "userId") Integer profile_id){
        Message message = new Message();
        Optional<ProfileResponseDto> profileResponseDto = profileService.getProfileByUserId(profile_id);

        if (profileResponseDto.isEmpty()){
            message.setStatus(HttpStatus.NOT_FOUND.value());
            message.setMessage(MsgCode.PROFILE_NOT_FOUND.getMsg());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(message);
        }
        else{
            message.setStatus(HttpStatus.OK.value());
            message.setMessage(MsgCode.READ_SUCCESS.getMsg());
            message.setData(profileResponseDto);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(message);
        }
    }

    //  닉네임 기준 프로필 조회
    @GetMapping(value = "profiles/nicknames/{nickname}")
    public ResponseEntity<Message> getProfileByNickname(@PathVariable(name = "nickname") String nickname){
        Message message = new Message();
        Optional<ProfileResponseDto> profileResponseDto = profileService.getProfileByNickname(nickname);

        if (profileResponseDto.isEmpty()){
            message.setStatus(HttpStatus.NOT_FOUND.value());
            message.setMessage(MsgCode.PROFILE_NOT_FOUND.getMsg());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(message);
        }
        else{
            message.setStatus(HttpStatus.OK.value());
            message.setMessage(MsgCode.READ_SUCCESS.getMsg());
            message.setData(profileResponseDto);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(message);
        }
    }

    //  프로필 생성, 이미지 미포함
    @PostMapping(value = "profiles/without-image")
    public ResponseEntity<Message> saveProfileJson(@RequestBody ProfileRequestDto profileRequestDto) {
        Message message = new Message();
        //  해당 유저의 프로필이 존재하는지 확인
        Optional<ProfileResponseDto> profileResponseDto = profileService.getProfileByUserId(profileRequestDto.getUserId());

        //  해당 유저의 프로필이 이미 존재함
        if (profileResponseDto.isPresent()){
            message.setStatus(HttpStatus.CONFLICT.value());
            message.setMessage(MsgCode.PROFILE_EXIT.getMsg());
            message.setData(profileResponseDto);
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(message);
        }

        //  프로필 생성
        profileResponseDto = profileService.saveProfile(profileRequestDto);

        //  프로필 생성에 실패함
        if (profileResponseDto.isEmpty()) {
            message.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            message.setMessage(MsgCode.PROFILE_CREATE_FAIL.getMsg());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(message);
        }
        //  프로필 생성 성공
        else {
            message.setStatus(HttpStatus.CREATED.value());
            message.setMessage(MsgCode.PROFILE_CREATE_SUCCESS.getMsg());
            message.setData(profileResponseDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(message);
        }
    }

    //  프로필 생성, 이미지 포함
    @PostMapping(value = "profiles/with-image", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Message> saveProfileMultipart(@RequestPart(name = "request") ProfileRequestDto profileRequestDto,
                                                        @RequestPart(name = "file", required = false) MultipartFile file) {
        Message message = new Message();
        //  해당 유저의 프로필이 존재하는지 확인
        Optional<ProfileResponseDto> profileResponseDto = profileService.getProfileByUserId(profileRequestDto.getUserId());

        //  해당 유저의 프로필이 이미 존재함
        if (profileResponseDto.isPresent()){
            message.setStatus(HttpStatus.CONFLICT.value());
            message.setMessage(MsgCode.PROFILE_EXIT.getMsg());
            message.setData(profileResponseDto);
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(message);
        }

        //  프로필 생성
        else {
            //  프로필 이미지 포함해서 반환
            if (file != null && !file.isEmpty()) {
                profileResponseDto = profileService.saveProfile(profileRequestDto, file);
            }
            //  프로필 이미지 미포함하여 반환, 프로필 이미지는 기본 이미지 url로 설정
            else {
                profileResponseDto = profileService.saveProfile(profileRequestDto);
            }
        }

        //  프로필 생성에 실패함
        if (profileResponseDto.isEmpty()) {
            message.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            message.setMessage(MsgCode.PROFILE_CREATE_FAIL.getMsg());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(message);
        }
        //  프로필 생성 성공
        else {
            message.setStatus(HttpStatus.CREATED.value());
            message.setMessage(MsgCode.PROFILE_CREATE_SUCCESS.getMsg());
            message.setData(profileResponseDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(message);
        }
    }

    //  프로필 수정, 이미지 미포함
    @PutMapping(value = "profiles")
    public ResponseEntity<Message> updateProfile(@RequestBody ProfileRequestDto profileRequestDto){
        Message message = new Message();
        Optional<ProfileResponseDto> profileResponseDto = profileService.updateProfile(profileRequestDto);

        if (profileResponseDto.isEmpty()){
            message.setStatus(HttpStatus.NOT_FOUND.value());
            message.setMessage(MsgCode.PROFILE_NOT_FOUND.getMsg());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(message);
        }
        else {
            message.setStatus(HttpStatus.OK.value());
            message.setMessage(MsgCode.UPDATE_SUCCESS.getMsg());
            message.setData(profileResponseDto);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(message);
        }
    }

    //  프로필 삭제, 이미지 미포함
    @DeleteMapping(value = "profiles/{profileId}")
    public ResponseEntity<Message> deleteProfile(@PathVariable(name = "profileId") Integer profileId){
        Message message = profileService.deleteProfile(profileId);

        return ResponseEntity.status(message.getStatus())
                .body(message);
    }
}
