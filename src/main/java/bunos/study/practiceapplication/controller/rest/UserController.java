package bunos.study.practiceapplication.controller.rest;

import bunos.study.practiceapplication.domain.dto.UserData;
import bunos.study.practiceapplication.domain.model.Role;
import bunos.study.practiceapplication.domain.model.User;
import bunos.study.practiceapplication.service.RoleService;
import bunos.study.practiceapplication.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/roles")
    public List<Role> getRolesByUser(@RequestBody UserData userData) {
        return userService.getUserRoles(userData);
    }
}
