package fr.thespacerangers.back.services;

import fr.thespacerangers.back.entities.User;
import fr.thespacerangers.back.exeptions.EntityAlreadyExistsException;
import fr.thespacerangers.back.repositories.UserRepository;
import fr.thespacerangers.back.services.interfaces.IAuthService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public String register(fr.thespacerangers.back.dto.auth.RegisterRequest request) {
        if (this.userRepository.existsByUsername(request.username()))
            throw new EntityAlreadyExistsException("error.user.already-exists");

        if (this.userRepository.existsByEmail(request.email()))
            throw new EntityAlreadyExistsException("error.user.already-exists");

        final User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(this.passwordEncoder.encode(request.password()))
                .balance(0.0f)
                .build();
        this.userRepository.save(user);

        return "success.user.created";
    }
}
