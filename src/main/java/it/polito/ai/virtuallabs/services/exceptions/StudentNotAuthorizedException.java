package it.polito.ai.virtuallabs.services.exceptions;

public class StudentNotAuthorizedException extends VirtualLabsServiceException {
    public StudentNotAuthorizedException() {
    }

    public StudentNotAuthorizedException(String msg) {
        super(msg);
    }
}
