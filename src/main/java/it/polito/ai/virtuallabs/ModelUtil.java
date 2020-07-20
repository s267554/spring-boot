package it.polito.ai.virtuallabs;

import it.polito.ai.virtuallabs.dtos.TeamDTO;
import it.polito.ai.virtuallabs.entities.Team;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;

public final class ModelUtil {

    private ModelUtil() {
    }

    public final static String USER_EMAIL_DOMAIN = "studenti.polito.it";
    public final static String ADMIN_EMAIL_DOMAIN = "polito.it";

    public final static String ROLE_USER = "ROLE_USER";
    public final static String ROLE_ADMIN = "ROLE_ADMIN";

    public static @NotNull TeamDTO map(@NotNull ModelMapper modelMapper, Team team) {
        final TeamDTO teamDTO = modelMapper.map(team, TeamDTO.class);
        final Team.Key key = team.getKey();
        teamDTO.setCourseName(key.getCourseName());
        teamDTO.setName(key.getName());
        return teamDTO;
    }

    public static @NotNull Team map(@NotNull ModelMapper modelMapper, TeamDTO teamDTO) {
        final Team team = modelMapper.map(teamDTO, Team.class);
        team.setKey(new Team.Key(teamDTO.getCourseName(), teamDTO.getName()));
        return team;
    }

}
