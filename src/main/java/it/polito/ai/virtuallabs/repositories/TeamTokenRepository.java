package it.polito.ai.virtuallabs.repositories;

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

}
