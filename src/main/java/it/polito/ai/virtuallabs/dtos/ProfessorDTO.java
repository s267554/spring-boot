package it.polito.ai.virtuallabs.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class ProfessorDTO extends RepresentationModel<ProfessorDTO> {

    @NotNull
    // @Pattern(regexp = ModelUtil.REGEX_ID)
    private String id;

    @NotNull
    //  @Pattern(regexp = ModelUtil.REGEX_EMAIL)
    private String email;

    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    private String photoUrl;

}
