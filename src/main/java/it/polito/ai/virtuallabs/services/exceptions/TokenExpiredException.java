package it.polito.ai.virtuallabs.services.exceptions;

public class TokenExpiredException extends NotificationServiceException {
    public TokenExpiredException() {
    }

    public TokenExpiredException(String message) {
        super(message);
    }
}
