package suftware.tuitui.ar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("ar/")
public class ARController {
    @GetMapping("pages")
    public String requestARPage(){
        return "ar";
    }
}
