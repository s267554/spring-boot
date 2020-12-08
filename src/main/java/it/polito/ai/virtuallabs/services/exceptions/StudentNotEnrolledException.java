package it.polito.ai.virtuallabs.services.exceptions;

public class StudentNotEnrolledException extends VirtualLabsServiceException {

    public StudentNotEnrolledException() {
    }

    public StudentNotEnrolledException(String msg) {
        super(msg);
    }

}
