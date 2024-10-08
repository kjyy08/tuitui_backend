package suftware.tuitui.admin.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import suftware.tuitui.dto.response.PageResponse;
import suftware.tuitui.service.UserService;

import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("admin/")
public class AdminController {

    //  login page
    @GetMapping("login")
    public String login(HttpServletRequest request, Model model){
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        model.addAttribute("_csrf", csrfToken);
        return "login";
    }

    // home page
    @GetMapping("home")
    public String home(HttpServletRequest request, Model model) {
        model.addAttribute("requestURI", request.getRequestURI());
        return "home"; // home.html 템플릿을 렌더링
    }

}
