package it.polito.ai.virtuallabs.services;

public class TeamNotEnabledException extends VirtualLabsServiceException {
    public TeamNotEnabledException() {
    }

    public TeamNotEnabledException(String msg) {
        super(msg);
    }
}
