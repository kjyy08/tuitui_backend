package suftware.tuitui.admin.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import suftware.tuitui.dto.response.PageResponse;
import suftware.tuitui.service.TimeCapsuleImageService;
import suftware.tuitui.service.TimeCapsuleService;

import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("admin/")
public class AdminCapsuleController {
    private final TimeCapsuleService timeCapsuleService;
    private final TimeCapsuleImageService timeCapsuleImageService;

    // Capsules page
    @GetMapping("capsules")
    public String capsules(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
                           @RequestParam(name = "pageSize", defaultValue = "15") Integer pageSize,
                           HttpServletRequest request, Model model) {
        Optional<PageResponse> timeCapsulePage = timeCapsuleService.getCapsuleList(pageNo, pageSize, "timeCapsuleId");

        model.addAttribute("capsulePage", timeCapsulePage.get());
        model.addAttribute("requestURI", request.getRequestURI());
        return "capsules"; // capsules.html 템플릿을 렌더링
    }

    @DeleteMapping("capsules/{capsuleId}")
    public ResponseEntity<Void> deleteCapsule(@PathVariable(name = "capsuleId") Integer id){
        timeCapsuleService.deleteCapsule(id);
        timeCapsuleImageService.deleteCapsuleImage(id);

        return ResponseEntity.ok().build();
    }
}
