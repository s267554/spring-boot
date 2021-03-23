package it.polito.ai.virtuallabs.repositories;

import it.polito.ai.virtuallabs.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

    @Query("select s from Student s where s.id = :username or s.email = :username")
    Optional<Student> findByUsername(String username);

    @Query("select s1 from Student s1 where s1.id not in " +
            "(select s2.id from Student s2 inner join s2.courses c where c.name = :courseName)")
    List<Student> findStudentsNotInCourse(String courseName);

    @Query(
            "select s1 from Student s1 inner join s1.courses c where c.name = :courseName and s1 not in" +
                    "(select s2 " +
                    "from Student s2 inner join s2.courses c inner join c.teams t " +
                    "where c.name = :courseName and t.enabled = true)"
    )
    List<Student> findStudentsNotInTeam(String courseName);

}
