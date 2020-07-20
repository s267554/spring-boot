package it.polito.ai.virtuallabs.repositories;

import it.polito.ai.virtuallabs.entities.Paper;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaperRepository extends CrudRepository<Paper, Paper.Key> {
}
