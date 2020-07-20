package it.polito.ai.virtuallabs.services;

import it.polito.ai.virtuallabs.dtos.TeamDTO;
import it.polito.ai.virtuallabs.dtos.UserDTO;

import java.util.List;

public interface NotificationService {

    void sendMessage(String address, String subject, String body);

    void confirmAccount(String token);

    void notifyAccount(UserDTO userDTO);

    boolean confirmTeam(String token);

    void rejectTeam(String token);

    void notifyTeam(TeamDTO teamDTO, List<String> ids, Long timeout);

}
