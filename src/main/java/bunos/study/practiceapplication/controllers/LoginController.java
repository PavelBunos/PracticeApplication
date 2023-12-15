package bunos.study.practiceapplication.controllers;

import bunos.study.practiceapplication.models.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signin")
public class LoginController {
    @GetMapping("/login")
    public void login(@RequestBody User user) {

    }
}
