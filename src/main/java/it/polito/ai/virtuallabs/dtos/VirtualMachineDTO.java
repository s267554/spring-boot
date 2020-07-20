package it.polito.ai.virtuallabs.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(callSuper = false)
public class VirtualMachineDTO extends RepresentationModel<VirtualMachineDTO> {

    private Long id;

    private int vcpu;

    private double space;

    private double ram;

}
