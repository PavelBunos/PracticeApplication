package bunos.study.practiceapplication.service;

import bunos.study.practiceapplication.domain.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    void add(User user);

    void remove(User user);

    User getById(long id);

    User update(User user);

    List<User> getAllUsers();
}
