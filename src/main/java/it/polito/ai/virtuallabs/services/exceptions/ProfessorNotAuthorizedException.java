package it.polito.ai.virtuallabs.services.exceptions;

public class ProfessorNotAuthorizedException extends VirtualLabsServiceException {
    public ProfessorNotAuthorizedException() {
    }

    public ProfessorNotAuthorizedException(String msg) {
        super(msg);
    }
}
