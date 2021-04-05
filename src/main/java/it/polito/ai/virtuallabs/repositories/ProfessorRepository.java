package it.polito.ai.virtuallabs.repositories;

import it.polito.ai.virtuallabs.entities.Professor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfessorRepository extends CrudRepository<Professor, String> {

    @Query("select p from Professor p where p.id = :username or p.email = :username")
    Optional<Professor> findByUsername(String username);

    @Query("select p1 from Professor p1 where p1.id not in " +
            "(select p2.id from Professor p2 inner join p2.courses c where c.name = :courseName)")
    List<Professor> findProfessorsNotInCharge(String courseName);
}
