package it.polito.ai.virtuallabs.services;

public class AssignmentExpiredException extends VirtualLabsServiceException {
    public AssignmentExpiredException() {
    }

    public AssignmentExpiredException(String msg) {
        super(msg);
    }
}
