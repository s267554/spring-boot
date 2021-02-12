package it.polito.ai.virtuallabs.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class VirtualMachineDTO extends RepresentationModel<VirtualMachineDTO> {

    private Long id;

    private int vcpu;

    private double space;

    private double ram;

    private String url;

    private boolean active;

    private List<StudentDTO> owners;

}
