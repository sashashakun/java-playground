package com.example.fintech.day7.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    public record TokenRequest(String username, String password) {}
    public record TokenResponse(String token) {}

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> token(@RequestBody TokenRequest request) {
        // Exercise shortcut: any username with password "password" is accepted.
        // In production, replace with a real UserDetailsService + PasswordEncoder check.
        if (!"password".equals(request.password())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<String> roles = "admin".equals(request.username())
                ? List.of("USER", "ADMIN")
                : List.of("USER");
        return ResponseEntity.ok(new TokenResponse(jwtService.generateToken(request.username(), roles)));
    }
}
