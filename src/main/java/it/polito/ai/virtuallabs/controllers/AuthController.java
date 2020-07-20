package it.polito.ai.virtuallabs.controllers;

import it.polito.ai.virtuallabs.dtos.UserDTO;
import it.polito.ai.virtuallabs.models.AuthRequest;
import it.polito.ai.virtuallabs.models.AuthResponse;
import it.polito.ai.virtuallabs.models.RegisterRequest;
import it.polito.ai.virtuallabs.security.JwtTokenProvider;
import it.polito.ai.virtuallabs.services.NotificationService;
import it.polito.ai.virtuallabs.services.UserService;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static it.polito.ai.virtuallabs.ModelUtil.*;

@Validated
@RestController
public class AuthController {

    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final ModelMapper modelMapper;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenProvider jwtTokenProvider;

    private final NotificationService notificationService;

    public AuthController(UserService userService,
                          NotificationService notificationService,
                          AuthenticationManager authenticationManager,
                          ModelMapper modelMapper,
                          PasswordEncoder passwordEncoder,
                          JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.notificationService = notificationService;
    }

    @PostMapping("/register")
    public void register(@Valid @RequestBody RegisterRequest registerRequest) {

        final UserDTO userDTO = valid(registerRequest);

        final String password = passwordEncoder.encode(registerRequest.getPassword1());
        userDTO.setPassword(password);

        userService.createUser(userDTO);

        notificationService.notifyAccount(userDTO);

    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody @NotNull AuthRequest authRequest) {

        final String username = authRequest.getUsername();

        final Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, authRequest.getPassword()));

        final List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        final String token = jwtTokenProvider.createToken(username, roles);

        return AuthResponse.builder()
                .expiry(jwtTokenProvider.getValidityInMilliseconds())
                .username(username)
                .token(token)
                .roles(roles)
                .build();

    }

    /**
     * This method validate the passed instance of RegisterRequest and returns a UserDTO populated with the data
     * retrieved from the request or calculated starting from them.
     *
     * @param registerRequest represents the remote data
     * @return a UserDTO populated and validated
     */
    private @NotNull UserDTO valid(@NotNull RegisterRequest registerRequest) {
        if (!registerRequest.getPassword1().equals(registerRequest.getPassword2())) {
            // If passwords are not the same, the request is invalid
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords are not equal");
        }
        String id = registerRequest.getId();
        String email = registerRequest.getEmail();
        if (id != null) {
            id = id.toLowerCase(Locale.US);
            if (email != null) {
                email = email.toLowerCase(Locale.US);
                final String[] split = email.split("@");
                if (!split[0].equals(id)) {
                    // The id retrieved from the email and the id of the request must be the same.
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email and id are not the same");
                }
                // If the id starts with s, the user is a student and its email domain should be @studenti.polito.it
                // otherwise the email domain must be @polito.it
                if (id.startsWith("s") && !split[1].equals(USER_EMAIL_DOMAIN) || !split[1].equals(ADMIN_EMAIL_DOMAIN)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad email domain");
                }
            } else {
                email = id + "@";
                if (id.startsWith("s")) {
                    email += USER_EMAIL_DOMAIN;
                } else {
                    email += ADMIN_EMAIL_DOMAIN;
                }
            }
        } else if (email != null) {
            email = email.toLowerCase(Locale.US);
            id = email.split("@")[0];
        } else {
            // No email neither id found, it's not possible to retrieve the id of the user
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username not specified");
        }

        final UserDTO userDTO = modelMapper.map(registerRequest, UserDTO.class);
        userDTO.setEmail(email);
        userDTO.setId(id);

        if (id.startsWith("s")) {
            userDTO.setRole(ROLE_USER);
        } else {
            userDTO.setRole(ROLE_ADMIN);
        }

        return userDTO;
    }

}
