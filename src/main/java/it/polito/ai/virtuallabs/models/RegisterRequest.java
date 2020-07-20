package it.polito.ai.virtuallabs.models;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class RegisterRequest {

    @Nullable
    @Pattern(regexp = "([pP]|[dD])[\\d]{6}")
    String id;

    @Nullable
    @Pattern(regexp = "([sS][\\d]{6}@studenti\\.polito\\.it)|([dD][\\d]{6}@polito\\.it)")
    String email;

    @NotBlank
    String name;

    @NotBlank
    String surname;

    @NotBlank
    @Size(min = 8, max = 24)
    String password1;

    @NotBlank
    @Size(min = 8, max = 24)
    String password2;

}
