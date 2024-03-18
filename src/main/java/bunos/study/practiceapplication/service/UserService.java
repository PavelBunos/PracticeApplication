package bunos.study.practiceapplication.service;

import bunos.study.practiceapplication.domain.dto.UserData;
import bunos.study.practiceapplication.domain.model.Role;
import bunos.study.practiceapplication.domain.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    ResponseEntity<?> create(UserData userData);

    ResponseEntity<?> remove(UserData userData);

    List<User> getAllUsers();

    List<Role> getUserRoles(UserData userData);
}
