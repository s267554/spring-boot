package it.polito.ai.virtuallabs.repositories;

import it.polito.ai.virtuallabs.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query("select u from User u where u.id = :username or u.email = :username")
    Optional<User> findByUsername(String username);

}
