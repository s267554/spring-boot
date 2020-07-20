package it.polito.ai.virtuallabs.services;

public class ProfessorNotFoundException extends VirtualLabsServiceException {
    public ProfessorNotFoundException() {
    }

    public ProfessorNotFoundException(String msg) {
        super(msg);
    }
}
