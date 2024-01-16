package bunos.study.practiceapplication.services;

import bunos.study.practiceapplication.models.security.User;
import bunos.study.practiceapplication.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Primary
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public void add(User user) {
        userRepository.save(user);
    }

    @Override
    public void remove(User user) {
        userRepository.delete(user);
    }

    @Override
    public User getById(long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public User update(User user) {
        userRepository.save(user);
        return user;
    }

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
