package it.polito.ai.virtuallabs.dtos;

import it.polito.ai.virtuallabs.ModelUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentDTO extends RepresentationModel<StudentDTO> {

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
