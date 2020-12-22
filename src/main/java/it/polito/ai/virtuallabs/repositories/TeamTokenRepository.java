package it.polito.ai.virtuallabs.repositories;

import it.polito.ai.virtuallabs.entities.Student;
import it.polito.ai.virtuallabs.entities.Team;
import it.polito.ai.virtuallabs.entities.TeamToken;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamTokenRepository extends CrudRepository<TeamToken, String> {

    @Query("select t from TeamToken t where t.team.key = :key")
    List<TeamToken> findAllByTeamId(Team.Key key);

    @Query("delete from TeamToken t where t.team.key = :key")
    void deleteAllByTeamId(Team.Key key);

    // delete method?
    @Query("select t.student from TeamToken t where t.team.key = :key")
    List<String> findPending(Team.Key key);

    @Query("select t from TeamToken t where t.team.key = :key and t.student = :student")
    TeamToken findByTeamIdAndStudent(Team.Key key, Student student);

    @Query("select t from TeamToken t where t.student.id = :studentId and t.team.course.name = :courseName")
    List<TeamToken> findByStudentAndCourseName(String studentId, String courseName);

}
