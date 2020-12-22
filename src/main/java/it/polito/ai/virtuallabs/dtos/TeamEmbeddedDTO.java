package it.polito.ai.virtuallabs.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class TeamEmbeddedDTO extends TeamDTO {

    private List<StudentDTO> members;

}
