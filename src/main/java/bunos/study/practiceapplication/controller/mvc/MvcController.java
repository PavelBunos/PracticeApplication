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
    @GetMapping("/")
    public String index() {
        return "index.html";
    }

    @GetMapping("/backup")
    public String backup() {
        return "backup.html";
    }

    @GetMapping("/settings")
    @PreAuthorize("hasRole('ADMIN')")
    public String settings() {
        return "settings.html";
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {
        return "admin.html";
    }
}
