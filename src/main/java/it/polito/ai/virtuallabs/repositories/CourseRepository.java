package it.polito.ai.virtuallabs.repositories;

import it.polito.ai.virtuallabs.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
}
