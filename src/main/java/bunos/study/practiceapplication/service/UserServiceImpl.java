package bunos.study.practiceapplication.service;

import bunos.study.practiceapplication.domain.dto.Response;
import bunos.study.practiceapplication.domain.dto.UserData;
import bunos.study.practiceapplication.domain.model.Role;
import bunos.study.practiceapplication.domain.model.User;
import bunos.study.practiceapplication.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Primary
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;

    @Override
    public ResponseEntity<?> create(UserData userData) {
        User user = userRepository.findAll().stream().filter(x -> x.getUsername().equals(userData.getUsername())).findFirst().orElse(null);

        if (user != null) {
            return new ResponseEntity<>(Response.builder().data(user.getUsername() + " уже существует!").build(), HttpStatus.BAD_REQUEST);
        }

        user = User.builder()
                .username(userData.getUsername())
                .password(userData.getPassword())
                .roles(List.of(roleService.getByName(userData.getRole())))
                .build();
        userRepository.save(user);

        return new ResponseEntity<>(Response.builder().data(user.getUsername() + " успешно зарегистрирован!").build(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> remove(UserData userData) {
        User user = userRepository.findAll().stream().filter(x -> x.getUsername().equals(userData.getUsername())).findFirst().  get();
        if (user == null) {
            return new ResponseEntity<>(Response.builder().data("Пользователь не существует!").build(), HttpStatus.BAD_REQUEST);
        }

        userRepository.delete(user);
        return new ResponseEntity<>(Response.builder().data(user.getUsername() + " был успешно удалён!").build(), HttpStatus.OK);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository
                .findAll()
                .stream()
                .filter(
                        user -> user
                                .getUsername()
                                .equals(username)
                )
                .findFirst()
                .get();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<Role> getUserRoles(UserData userData) {
        return roleService.getAllRoles().stream().filter(x -> x.getName().equals(userData.getRole())).toList();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), new BCryptPasswordEncoder().encode(user.getPassword()), user.getRoles().stream().map(
                role -> new SimpleGrantedAuthority(role.getName())
        ).collect(Collectors.toList())
        );
    }
}
