package fr.thespacerangers.back.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull @NotBlank @NotEmpty @Email @JsonProperty("email") String email,
        @NotNull @NotBlank @NotEmpty @JsonProperty("password") String password
) {
}
