package bunos.study.practiceapplication.controllers;

import bunos.study.practiceapplication.models.User;
import bunos.study.practiceapplication.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping("/add")
    public void addUser(@RequestBody User user) {
        userService.add(user);
    }

    @DeleteMapping("/remove/{id}")
    public void removeUser(@PathVariable long id) {
        userService.remove(userService.getById(id));
    }

    @PutMapping("/update/{id}")
    public User updateUser(@PathVariable long id) {
        return userService.update(userService.getById(id));
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable long id) {
        return userService.getById(id);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}
