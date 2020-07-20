package it.polito.ai.virtuallabs.repositories;

import it.polito.ai.virtuallabs.entities.Assignment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends CrudRepository<Assignment, Long> {

    @Query("select a from Assignment a inner join a.course c where c.name = :courseName")
    List<Assignment> findAllByCourseName(String courseName);

}
