package it.polito.ai.virtuallabs.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "teams")
public class Team {

    @Data
    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Key implements Serializable {

        String courseName;

        String name;

    }

    @EmbeddedId
    Key key;

    @Column(name = "enabled")
    boolean enabled;

    @Min(0)
    @Column(name = "vcpu")
    int vcpu;

    @Min(0)
    @Column(name = "space")
    double space;

    @Min(0)
    @Column(name = "ram")
    double ram;

    @Min(0)
    @Column(name = "max_vms_active")
    int maxVMsActive;

    @Min(0)
    @Column(name = "max_vms")
    int maxVMs;

    @MapsId("courseName")
    @JoinColumn(name = "course_name")
    @ManyToOne
    Course course;

    @Column(name = "creator")
    String creator;

    @ElementCollection
    List<String> confirmedIds;

    @ElementCollection
    List<String> rejectedIds;

    @Column(name = "invalid")
    boolean invalid;

    // inefficient
    @Type(type = "timestamp")
    @Column(name = "expiry_date", nullable = false, updatable = false)
    Date expiryDate;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "team_student",
            joinColumns = {
                    @JoinColumn(name = "team_name"),
                    @JoinColumn(name = "course_name")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "student_id")
            }
    )
    List<Student> members = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "vm_model")
    VirtualMachineModel virtualMachineModel;

    @OneToMany(mappedBy = "team")
    List<VirtualMachine> virtualMachines = new ArrayList<>();

    public void addMember(@NotNull Student student) {
        student.teams.add(this);
        members.add(student);
    }

    public void setCourse(@NotNull Course course) {
        course.teams.add(this);
        this.course = course;
    }

    public void addVirtualMachine(VirtualMachine virtualMachine) {
        virtualMachines.add(virtualMachine);
        virtualMachine.team = this;
    }

}
