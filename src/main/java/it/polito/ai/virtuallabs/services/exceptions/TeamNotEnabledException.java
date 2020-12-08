package it.polito.ai.virtuallabs.services.exceptions;

public class TeamNotEnabledException extends VirtualLabsServiceException {
    public TeamNotEnabledException() {
    }

    public TeamNotEnabledException(String msg) {
        super(msg);
    }
}
