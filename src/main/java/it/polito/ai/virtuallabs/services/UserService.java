package it.polito.ai.virtuallabs.services;

import it.polito.ai.virtuallabs.dtos.UserDTO;
import it.polito.ai.virtuallabs.services.exceptions.UsernameAlreadyExistsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {

    @Override
    UserDTO loadUserByUsername(String username) throws UsernameNotFoundException;

    void createUser(UserDTO userDTO) throws UsernameAlreadyExistsException;

}
