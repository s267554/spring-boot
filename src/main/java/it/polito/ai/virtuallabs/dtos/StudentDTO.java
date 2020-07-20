package it.polito.ai.virtuallabs.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudentDTO extends RepresentationModel<StudentDTO> {

    private String id;

    private String email;

    private String name;

    private String surname;

    private String photoUrl;

}
