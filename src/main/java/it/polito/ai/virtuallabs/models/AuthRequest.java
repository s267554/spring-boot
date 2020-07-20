package it.polito.ai.virtuallabs.models;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AuthRequest {

    @NotBlank
    String username;

    @NotBlank
    String password;

}
