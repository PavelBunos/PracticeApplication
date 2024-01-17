package bunos.study.practiceapplication.controllers.mvc;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping
public class MvcController {
    @GetMapping("/index")
    public String index() {
        return "index.html";
    }

    @GetMapping("/backup")
    public String backup() {
        return "backup.html";
    }
}
