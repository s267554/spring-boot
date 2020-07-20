package it.polito.ai.virtuallabs.entities;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
@Table(name = "paper_version")
public class PaperVersion {

    @Id
    @GeneratedValue
    Long id;

    @NotNull
    @Type(type = "timestamp")
    @Column(name = "date", updatable = false, nullable = false)
    Date date;

    @NotNull
    @Column(name = "content_url", updatable = false, nullable = false)
    String contentUrl;

    @ManyToOne
    @JoinColumn(name = "assignment_id", nullable = false)
    @JoinColumn(name = "student_id", nullable = false)
    Paper paper;

    public void addPaper(@org.jetbrains.annotations.NotNull Paper paper) {
        paper.versions.add(this);
        this.paper = paper;
    }

}
