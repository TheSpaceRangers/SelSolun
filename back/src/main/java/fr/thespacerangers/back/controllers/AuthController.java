package fr.thespacerangers.back.controllers;

import fr.thespacerangers.back.dto.auth.LoginRequest;
import fr.thespacerangers.back.dto.auth.LoginResponse;
import fr.thespacerangers.back.dto.auth.RegisterRequest;
import fr.thespacerangers.back.services.interfaces.IAuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.authService.login(request));
    }
}
