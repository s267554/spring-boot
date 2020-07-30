package it.polito.ai.virtuallabs.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = false)
public class TeamDTO extends RepresentationModel<TeamDTO> {

    @NotBlank
    private String courseName;

    @NotBlank
    private String name;

    @Min(0)
    private int vcpu;

    @Min(0)
    private double space;

    @Min(0)
    private double ram;

    @Min(0)
    private int maxVMsActive;

    @Min(0)
    private int maxVMs;

    private boolean enabled;

}
