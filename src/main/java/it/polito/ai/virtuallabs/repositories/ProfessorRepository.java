package it.polito.ai.virtuallabs.repositories;

import it.polito.ai.virtuallabs.entities.Professor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfessorRepository extends CrudRepository<Professor, String> {

    @Query("select p from Professor p where p.id = :username or p.email = :username")
    Optional<Professor> findByUsername(String username);

}
