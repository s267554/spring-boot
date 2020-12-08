package it.polito.ai.virtuallabs.services.exceptions;

public class PaperAlreadyExistsException extends VirtualLabsServiceException {
    public PaperAlreadyExistsException() {
    }

    public PaperAlreadyExistsException(String message) {
        super(message);
    }
}
