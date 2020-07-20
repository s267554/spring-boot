package it.polito.ai.virtuallabs.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

import static it.polito.ai.virtuallabs.ModelUtil.ROLE_ADMIN;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(ROLE_ADMIN)
public class Professor extends User {

    @ManyToMany(mappedBy = "professors")
    List<Course> courses = new ArrayList<>();

    public void addCourse(@NotNull Course course) {
        course.professors.add(this);
        courses.add(course);
    }

}
