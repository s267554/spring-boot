package it.polito.ai.virtuallabs.entities;

import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "assignemnts")
public class Assignment {

    @Id
    @GeneratedValue
    Long id;

    @NotNull
    @Type(type = "timestamp")
    Date creationDate;

    @NotNull
    @Type(type = "timestamp")
    Date expiryDate;

    @URL
    @NotBlank
    @Column(name = "content_url", nullable = false, updatable = false)
    String contentUrl;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "course_name")
    Course course;

    @OneToMany(mappedBy = "assignment")
    List<Paper> papers = new ArrayList<>();

    public void setCourse(@org.jetbrains.annotations.NotNull Course course) {
        course.assignments.add(this);
        this.course = course;
    }

    public void addPaper(@org.jetbrains.annotations.NotNull Paper paper) {
        paper.assignment = this;
        papers.add(paper);
    }

}
