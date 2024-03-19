package bunos.study.practiceapplication.controller.rest;

import bunos.study.practiceapplication.domain.dto.JwtRequest;
import bunos.study.practiceapplication.domain.dto.JwtResponse;
import bunos.study.practiceapplication.domain.dto.UserData;
import bunos.study.practiceapplication.domain.dto.exception.AppError;
import bunos.study.practiceapplication.service.LogService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authManager;
    private final LogService logService;

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) { // todo вынести в сервис
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

        logService.log("Пользователь авторизован", userService.findByUsername(authRequest.getUsername()), HttpStatus.OK.value());

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
