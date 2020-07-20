package it.polito.ai.virtuallabs.services;

public class TokenNotFoundException extends NotificationServiceException {
    public TokenNotFoundException() {
    }

    public TokenNotFoundException(String message) {
        super(message);
    }
}
