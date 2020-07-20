package it.polito.ai.virtuallabs.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = true)
public class CourseDTO extends RepresentationModel<CourseDTO> {

    @NotBlank
    private String name;

    @NotBlank
    private String acronym;

    private boolean enabled;

    @Min(1)
    private int max;

    @Min(1)
    private int min;

}
