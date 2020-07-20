package it.polito.ai.virtuallabs.entities;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @NotBlank
    @Column(name = "name", nullable = false, updatable = false)
    String name;

    @NotBlank
    @Column(name = "acronym", nullable = false, updatable = false)
    String acronym;

    @Column(name = "enabled")
    boolean enabled;

    @Min(1)
    @Column(name = "max")
    int max;

    @Min(1)
    @Column(name = "min")
    int min;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "course_student",
            joinColumns = {@JoinColumn(name = "course_name")},
            inverseJoinColumns = {@JoinColumn(name = "student_id")}
    )
    List<Student> students = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "course_professor",
            joinColumns = {@JoinColumn(name = "course_name")},
            inverseJoinColumns = {@JoinColumn(name = "professor_id")}
    )
    List<Professor> professors = new ArrayList<>();

    @OneToMany(mappedBy = "course")
    List<Team> teams = new ArrayList<>();

    @OneToMany(mappedBy = "course")
    List<Assignment> assignments = new ArrayList<>();

    public void addStudent(@NotNull Student student) {
        student.courses.add(this);
        students.add(student);
    }

    public void removeStudent(@NotNull Student student) {
        student.courses.remove(this);
        students.remove(student);
    }

    public void addProfessor(@NotNull Professor professor) {
        professor.courses.add(this);
        professors.add(professor);
    }

    public void addTeam(@NotNull Team team) {
        team.course = this;
        teams.add(team);
    }

    public void addAssignment(@NotNull Assignment assignment) {
        assignment.course = this;
        assignments.add(assignment);
    }

}
