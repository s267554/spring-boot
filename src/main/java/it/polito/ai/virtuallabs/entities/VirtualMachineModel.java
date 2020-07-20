package it.polito.ai.virtuallabs.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "vm_models")
public class VirtualMachineModel {

    @Id
    @GeneratedValue
    Long id;

    @NotBlank
    @Column(name = "name", nullable = false, updatable = false)
    String name;

    String url;

}
