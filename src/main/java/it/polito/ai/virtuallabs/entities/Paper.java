package it.polito.ai.virtuallabs.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "papers")
public class Paper {

    @Data
    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Key implements Serializable {

        Long assignmentId;

        String studentId;

    }

    @EmbeddedId
    Key key;

    @Size(max = 33)
    @Column(name = "vote")
    Integer vote;

    @Column(name = "enabled")
    boolean enabled;

    @Column(name = "status")
    String status;

    @ManyToOne
    @MapsId("assignmentId")
    @JoinColumn(name = "assignment_id", nullable = false)
    Assignment assignment;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id", nullable = false)
    Student student;

    @OneToMany(mappedBy = "paper")
    List<PaperVersion> versions = new ArrayList<>();

    public void addPaperVersion(@NotNull PaperVersion paperVersion) {
        paperVersion.paper = this;
        versions.add(paperVersion);
    }

    public void setStudent(@NotNull Student student) {
        student.papers.add(this);
        this.student = student;
    }

    public void setAssignment(@NotNull Assignment assignment) {
        assignment.papers.add(this);
        this.assignment = assignment;
    }

}
