package it.polito.ai.virtuallabs.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class StudentEmbeddedDTO extends StudentDTO {

    private List<TeamDTO> teams;

}

