package fr.thespacerangers.back.services;

import fr.thespacerangers.back.dto.auth.LoginRequest;
import fr.thespacerangers.back.dto.auth.RegisterRequest;
import fr.thespacerangers.back.entities.User;
import fr.thespacerangers.back.exeptions.EntityAlreadyExistsException;
import fr.thespacerangers.back.exeptions.InvalidCredentialsException;
import fr.thespacerangers.back.repositories.UserRepository;
import fr.thespacerangers.back.services.interfaces.IAuthService;
import fr.thespacerangers.back.services.interfaces.IJwtService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private final UserRepository userRepository;

    private final IJwtService jwtService;

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public String register(RegisterRequest request) {
        if (this.userRepository.existsByUsername(request.username()))
            throw new EntityAlreadyExistsException("error.user.already-exists");

        if (this.userRepository.existsByEmail(request.email()))
            throw new EntityAlreadyExistsException("error.user.already-exists");

        final User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(this.passwordEncoder.encode(request.password()))
                .balance(0.0)
                .build();
        this.userRepository.save(user);

        return "success.user.created";
    }

    @Override
    public String login(LoginRequest request) {
        this.userRepository.findByEmail(request.email())
                .orElseThrow(() -> new InvalidCredentialsException("error.authentication.invalid-credentials"));

        try {
            final Authentication authentication = this.authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return this.jwtService.generateToken(this.userDetailsService.loadUserByUsername(request.email()));
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("error.authentication.invalid-credentials");
        }
    }
}
