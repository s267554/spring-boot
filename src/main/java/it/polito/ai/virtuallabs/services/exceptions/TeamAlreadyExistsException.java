package it.polito.ai.virtuallabs.services.exceptions;

public class TeamAlreadyExistsException extends VirtualLabsServiceException {

    public TeamAlreadyExistsException() {
    }

    public TeamAlreadyExistsException(String msg) {
        super(msg);
    }
}
