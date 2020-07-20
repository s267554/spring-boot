package it.polito.ai.virtuallabs.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class AssignmentDTO extends RepresentationModel<AssignmentDTO> {

    Long id;

    @NotNull
    Date creationDate;

    @NotNull
    Date expiryDate;

    @NotBlank
    String contentUrl;

}
