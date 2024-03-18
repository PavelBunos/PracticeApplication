package bunos.study.practiceapplication.controller.rest;

import bunos.study.practiceapplication.domain.dto.JwtRequest;
import bunos.study.practiceapplication.domain.dto.JwtResponse;
import bunos.study.practiceapplication.domain.dto.Response;
import bunos.study.practiceapplication.domain.dto.UserData;
import bunos.study.practiceapplication.domain.dto.exception.AppError;
import bunos.study.practiceapplication.service.UserService;
import bunos.study.practiceapplication.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController { // todo вынести в сервис
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authManager;

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Некорректный логин или пароль!"), HttpStatus.UNAUTHORIZED);
        }

        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/registration")
    public ResponseEntity<?> createNewUser(@RequestBody UserData userData) {
        return userService.create(userData);
    }

    @PostMapping("/remove")
    public ResponseEntity<?> deleteUser(@RequestBody UserData userData) {
        return userService.remove(userData);
    }
}
