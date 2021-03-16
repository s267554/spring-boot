package it.polito.ai.virtuallabs.controllers;

import it.polito.ai.virtuallabs.dtos.UserDTO;
import it.polito.ai.virtuallabs.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/{userId}")
    public UserDTO getUserProfile(@PathVariable String userId) {
        return userService.loadUserByUsername(userId);
    }
}
