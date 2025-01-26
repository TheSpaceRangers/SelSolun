package fr.thespacerangers.back.controllers;

import fr.thespacerangers.back.services.interfaces.IJwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/jwt")
@RequiredArgsConstructor
public class JWTController {
    private final IJwtService jwtService;

    @GetMapping("/expired")
    public ResponseEntity<Boolean> validate(
            @RequestHeader("Authorization") String authHeader
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.jwtService.isTokenExpired(authHeader));
    }
}
