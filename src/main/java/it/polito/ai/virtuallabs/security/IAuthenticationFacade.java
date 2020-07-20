package it.polito.ai.virtuallabs.security;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {

    Authentication getAuthentication();

}
