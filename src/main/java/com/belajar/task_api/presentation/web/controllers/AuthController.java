package com.belajar.task_api.presentation.web.controllers;
import org.springframework.beans.factory.annotation.Value;
import com.belajar.task_api.application.UserRepository;
import com.belajar.task_api.application.common.Result;
import com.belajar.task_api.application.dto.requests.AuthRequest;
import com.belajar.task_api.application.dto.responses.AuthResponse;
import com.belajar.task_api.domain.entities.User;
import com.belajar.task_api.infrastructure.security.JwtUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Value("${app.auth.simulated-username}")
    private String configuredUsername;

    @Value("${app.auth.simulated-password}")
    private String configuredPassword;

    @Value("${app.auth.simulated-role}")
    private String configuredRole;

    @PostMapping("/register")
    public String register(@RequestBody AuthRequest request) {
        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .build();
        userRepository.save(user);
        return "User berhasil didaftarkan!";
    }

    @PostMapping("/loginv1")
    public String loginv1(@RequestBody AuthRequest request) {
        log.info("Seseorang mencoba login dengan username: {}", request.username());
        return userRepository.findByUsername(request.username())
                .filter(u -> passwordEncoder.matches(request.password(), u.getPassword()))
                .map(u -> jwtUtils.generateToken1(u.getUsername()))
                .orElse("Username atau Password salah!");
    }

    @PostMapping("/loginv2")
    public ResponseEntity<?> loginv2(@Valid @RequestBody AuthRequest request) {

        if (configuredUsername.equals(request.username()) && configuredPassword.equals(request.password())) {
            String token = jwtUtils.generateToken(request.username(), configuredRole);

            AuthResponse response = new AuthResponse(token, request.username(), configuredRole);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Result.success(response));
        }
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Result.failure("Username atau password salah"));
    }
}