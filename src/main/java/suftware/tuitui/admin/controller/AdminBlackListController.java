package suftware.tuitui.admin.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
        return "blacklist";
    }

    @DeleteMapping("blacklists/{ipId}")
    public ResponseEntity<Void> blackList(@PathVariable(name = "ipId") Integer ipId){
        ipBlackListService.deleteIp(ipId);

        return ResponseEntity.ok().build();
    }
}
