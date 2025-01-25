package fr.thespacerangers.back.services.interfaces;

import fr.thespacerangers.back.dto.auth.RegisterRequest;

public interface IAuthService {
    String register (RegisterRequest request);
}
