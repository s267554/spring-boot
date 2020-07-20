package it.polito.ai.virtuallabs.repositories;

import it.polito.ai.virtuallabs.entities.PaperVersion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaperVersionRepository extends CrudRepository<PaperVersion, Long> {
}
