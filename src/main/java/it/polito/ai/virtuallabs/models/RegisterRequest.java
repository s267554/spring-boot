package it.polito.ai.virtuallabs.models;

import it.polito.ai.virtuallabs.ModelUtil;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class RegisterRequest {

    @Nullable
    @Pattern(regexp = ModelUtil.REGEX_ID)
    String id;

    @Nullable
    @Pattern(regexp = ModelUtil.REGEX_EMAIL)
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
