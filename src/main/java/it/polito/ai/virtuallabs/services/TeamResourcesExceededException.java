package it.polito.ai.virtuallabs.services;

public class TeamResourcesExceededException extends VirtualLabsServiceException {
    public TeamResourcesExceededException() {
    }

    public TeamResourcesExceededException(String msg) {
        super(msg);
    }
}
