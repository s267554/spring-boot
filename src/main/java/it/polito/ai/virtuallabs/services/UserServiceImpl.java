package it.polito.ai.virtuallabs.services;

import it.polito.ai.virtuallabs.dtos.UserDTO;
import it.polito.ai.virtuallabs.entities.Professor;
import it.polito.ai.virtuallabs.entities.Student;
import it.polito.ai.virtuallabs.entities.User;
import it.polito.ai.virtuallabs.repositories.UserRepository;
import it.polito.ai.virtuallabs.services.exceptions.UsernameAlreadyExistsException;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static it.polito.ai.virtuallabs.ModelUtil.ROLE_ADMIN;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDTO loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = loadUser(username);
        return modelMapper.map(user, UserDTO.class);
    }

    @Override
    public void createUser(UserDTO userDTO) throws UsernameAlreadyExistsException {

        if (userRepository.existsById(userDTO.getId())) {
            throw new UsernameAlreadyExistsException("User " + userDTO.getId() + " already exists");
        }

        final User user;
        if (ROLE_ADMIN.equals(userDTO.getRole())) {
            user = modelMapper.map(userDTO, Professor.class);
        } else {
            user = modelMapper.map(userDTO, Student.class);
        }

        userRepository.save(user);
    }

    private User loadUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
    }

}
