package suftware.tuitui.admin.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import suftware.tuitui.common.enumType.S3ImagePath;
import suftware.tuitui.dto.response.PageResponse;
import suftware.tuitui.service.ProfileImageService;
import suftware.tuitui.service.ProfileService;

import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("admin/")
public class AdminProfileController {
    private final ProfileService profileService;
    private final ProfileImageService profileImageService;

    // Profiles page
    @GetMapping("profiles")
    public String profiles(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
                           @RequestParam(name = "pageSize", defaultValue = "15") Integer pageSize,
                           HttpServletRequest request, Model model) {
        Optional<PageResponse> profilePage = profileService.getProfileList(pageNo, pageSize, "profileId");

        model.addAttribute("profilePage", profilePage.get());
        model.addAttribute("requestURI", request.getRequestURI());
        return "profiles"; // profiles.html 템플릿을 렌더링
    }

    @DeleteMapping("profiles/{profileId}")
    public ResponseEntity<Void> deleteProfile(@PathVariable(name = "profileId") Integer id) {
        profileImageService.deleteProfileImage(id, S3ImagePath.PROFILE.getPath());
        profileService.deleteProfile(id);
        return ResponseEntity.ok().build(); // 성공 시 응답 반환
    }
}
