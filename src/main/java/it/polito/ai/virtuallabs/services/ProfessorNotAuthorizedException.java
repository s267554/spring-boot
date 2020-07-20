package it.polito.ai.virtuallabs.services;

public class ProfessorNotAuthorizedException extends VirtualLabsServiceException {
    public ProfessorNotAuthorizedException() {
    }

    public ProfessorNotAuthorizedException(String msg) {
        super(msg);
    }
}
