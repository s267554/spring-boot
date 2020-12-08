package it.polito.ai.virtuallabs.services.exceptions;

public class AssignmentExpiredException extends VirtualLabsServiceException {
    public AssignmentExpiredException() {
    }

    public AssignmentExpiredException(String msg) {
        super(msg);
    }
}
