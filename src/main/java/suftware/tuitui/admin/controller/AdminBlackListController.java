package suftware.tuitui.admin.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import suftware.tuitui.admin.service.IpBlackListService;
import suftware.tuitui.dto.response.PageResponse;

import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("admin/")
public class AdminBlackListController {
    private final IpBlackListService ipBlackListService;

    @GetMapping("blacklists")
    public String blackList(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
                            @RequestParam(name = "pageSize", defaultValue = "15") Integer pageSize,
                            HttpServletRequest request, Model model){
        Optional<PageResponse> blackListPage = ipBlackListService.readIpBlackList(pageNo, pageSize, "ipId");

        model.addAttribute("blackListPage", blackListPage.get());
        model.addAttribute("requestURI", request.getRequestURI());
        return "blacklist"; // users.html 템플릿을 렌더링
    }
}
