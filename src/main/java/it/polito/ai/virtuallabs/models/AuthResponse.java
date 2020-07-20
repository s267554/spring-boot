package it.polito.ai.virtuallabs.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AuthResponse {

    String token;

    String username;

    List<String> roles;

    Long expiry;

}
