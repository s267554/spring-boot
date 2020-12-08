package it.polito.ai.virtuallabs.services.exceptions;

public class AssignmentNotFoundException extends VirtualLabsServiceException {
    public AssignmentNotFoundException() {
    }

    public AssignmentNotFoundException(String msg) {
        super(msg);
    }
}
