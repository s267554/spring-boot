package it.polito.ai.virtuallabs.services.exceptions;

public class NotificationServiceException extends RuntimeException {
    public NotificationServiceException() {
    }

    public NotificationServiceException(String message) {
        super(message);
    }
}
