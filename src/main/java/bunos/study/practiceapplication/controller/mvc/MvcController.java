package bunos.study.practiceapplication.controller.mvc;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping
public class MvcController {
    @GetMapping({"/home", "/backup", "/migration"})
    public String index() {
        return "index.html";
    }

    @GetMapping({"/admin", "/settings"})
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {
        return "index.html";
    }

    @GetMapping("/login")
    public String login() {
        return "index.html";
    }
}
