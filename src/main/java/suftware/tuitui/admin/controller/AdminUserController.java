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
import suftware.tuitui.service.TimeCapsuleImageService;
import suftware.tuitui.service.UserService;

import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("admin/")
public class AdminUserController {
    private final UserService userService;
    private final ProfileImageService profileImageService;
    private final TimeCapsuleImageService timeCapsuleImageService;

    // Users page
    @GetMapping("users")
    public String users(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
                        @RequestParam(name = "pageSize", defaultValue = "15") Integer pageSize,
                        HttpServletRequest request, Model model) {
        Optional<PageResponse> userPage = userService.getUserList(pageNo, pageSize, "userId");

        model.addAttribute("userPage", userPage.get());
        model.addAttribute("requestURI", request.getRequestURI());
        return "users"; // users.html 템플릿을 렌더링
    }

    @DeleteMapping("users/{userId}")
    public ResponseEntity<Void> deleteProfile(@PathVariable(name = "userId") Integer id) {
        profileImageService.deleteProfileImageS3(id);
        timeCapsuleImageService.deleteCapsuleImageS3(id);
        userService.deleteUser(id);

        return ResponseEntity.ok().build();
    }
}
