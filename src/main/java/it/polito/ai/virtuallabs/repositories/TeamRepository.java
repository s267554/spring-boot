package it.polito.ai.virtuallabs.repositories;

import it.polito.ai.virtuallabs.entities.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamRepository extends CrudRepository<Team, Team.Key> {

    @Query("select t from Team t inner join t.course c inner join c.students s " +
            "where t.enabled = true and c.name = :courseName and (s.id = :username or s.email = :username)")
    List<Team> findByCourseAndStudent(String courseName, String username);

}
