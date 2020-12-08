package it.polito.ai.virtuallabs.services.exceptions;

public class TeamNotFoundException extends VirtualLabsServiceException {
    public TeamNotFoundException() {
    }

    public TeamNotFoundException(String msg) {
        super(msg);
    }
}
