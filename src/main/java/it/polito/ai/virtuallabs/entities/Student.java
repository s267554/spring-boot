package it.polito.ai.virtuallabs.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static it.polito.ai.virtuallabs.ModelUtil.ROLE_USER;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(ROLE_USER)
public class Student extends User {

    @ManyToMany(mappedBy = "students")
    List<Course> courses = new ArrayList<>();

    @ManyToMany(mappedBy = "members")
    List<Team> teams = new ArrayList<>();

    @ManyToMany(mappedBy = "owners")
    List<VirtualMachine> virtualMachines = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    List<Paper> papers = new ArrayList<>();

    public void addTeam(@NotNull Team team) {
        team.members.add(this);
        teams.add(team);
    }

    public void addCourse(@NotNull Course course) {
        course.students.add(this);
        courses.add(course);
    }

    public void removeCourse(@NotNull Course course) {
        course.students.remove(this);
        courses.remove(course);
    }

    public void addVirtualMachine(@NotNull VirtualMachine virtualMachine) {
        virtualMachine.owners.add(this);
        virtualMachines.add(virtualMachine);
    }

    public void addPaper(@NotNull Paper paper) {
        paper.student = this;
        papers.add(paper);
    }

}
