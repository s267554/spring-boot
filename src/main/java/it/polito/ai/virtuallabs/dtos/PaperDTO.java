package it.polito.ai.virtuallabs.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(callSuper = true)
public class PaperDTO extends RepresentationModel<PaperDTO> {

    Long assignmentId;

    String studentId;

    Integer vote;

    boolean enabled;

    String status;
}
