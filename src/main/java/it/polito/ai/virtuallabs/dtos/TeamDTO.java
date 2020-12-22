package it.polito.ai.virtuallabs.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

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

    private String creator;

    private List<String> confirmedIds;

    private List<String> rejectedIds;

    private boolean invalid;

    private Date expiryDate;

}
