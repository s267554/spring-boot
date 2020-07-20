package it.polito.ai.virtuallabs.entities;

import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "vms")
public class VirtualMachine {

    @Id
    @GeneratedValue
    Long id;

    @URL
    @NotNull
    @Column(name = "url", nullable = false)
    String url;

    @Column(name = "active")
    boolean active;

    @Min(1)
    @Column(name = "vcpu")
    int vcpu;

    @Min(1)
    @Column(name = "space")
    double space;

    @Min(1)
    @Column(name = "ram")
    double ram;

    @ManyToOne
    @JoinTable(
            name = "team_vm",
            inverseJoinColumns = {
                    @JoinColumn(name = "course_name"),
                    @JoinColumn(name = "team_name")
            },
            joinColumns = {
                    @JoinColumn(name = "vm_id")
            }
    )
    Team team;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "vm_student",
            joinColumns = {@JoinColumn(name = "vm_id")},
            inverseJoinColumns = {@JoinColumn(name = "student_id")}
    )
    List<Student> owners = new ArrayList<>();

    @ManyToOne
    VirtualMachineModel vmModel;

    public void setTeam(@NotNull Team team) {
        team.virtualMachines.add(this);
        this.team = team;
    }

    public void addOwner(@NotNull Student student) {
        student.virtualMachines.add(this);
        owners.add(student);
    }

}
