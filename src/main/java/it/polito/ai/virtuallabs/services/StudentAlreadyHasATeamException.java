package it.polito.ai.virtuallabs.services;

public class StudentAlreadyHasATeamException extends VirtualLabsServiceException {
    public StudentAlreadyHasATeamException() {
    }

    public StudentAlreadyHasATeamException(String msg) {
        super(msg);
    }
}
