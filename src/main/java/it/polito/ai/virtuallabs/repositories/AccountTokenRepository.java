package it.polito.ai.virtuallabs.repositories;

import it.polito.ai.virtuallabs.entities.AccountToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTokenRepository extends CrudRepository<AccountToken, String> {
}
