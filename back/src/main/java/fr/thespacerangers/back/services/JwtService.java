package fr.thespacerangers.back.services;

import fr.thespacerangers.back.entities.User;
import fr.thespacerangers.back.repositories.UserRepository;
import fr.thespacerangers.back.services.interfaces.IJwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import jakarta.annotation.PostConstruct;

import jakarta.transaction.Transactional;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService implements IJwtService {
    private final UserRepository userRepository;

    private SecretKey key;

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    private String buildToken (
            Map<String, Object> claims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    private <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver
    ) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(
            String token
    ) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            throw new RuntimeException("Token invalid ou expiré");
        }
    }

    @Override
    public String generateToken(
            UserDetails userDetails
    ) {
        final Map<String, Object> claims = new HashMap<>();
        final User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec email : " + userDetails.getUsername()));
        claims.put("balance", user.getBalance() != null ? user.getBalance() : 0.0);

        return this.generateToken(claims, userDetails);
    }

    @Override
    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return this.buildToken(extraClaims, userDetails, jwtExpiration);
    }

    @Override
    @Transactional
    public boolean validateToken(
            String token,
            UserDetails userDetails
    ) {
        return this.getUsernameFromToken(token).equals(userDetails.getUsername()) && this.isTokenExpired(token);
    }

    @Override
    public String getUsernameFromToken(
            String token
    ) {
        return this.extractClaim(token, Claims::getSubject);
    }

    @Override
    public Double getBalanceFromToken(
            String token
    ) {
        return this.extractClaim(token, claims -> claims.get("balance", Double.class));
    }

    @Override
    public Date getExpirationDateFromToken(
            String token
    ) {
        return this.extractClaim(token, Claims::getExpiration);
    }

    @Override
    public boolean isTokenExpired(
            String token
    ) {
        return !this.getExpirationDateFromToken(token).before(new Date());
    }
}
