package it.polito.ai.virtuallabs.services;

public class StudentNotEnrolledException extends VirtualLabsServiceException {

    public StudentNotEnrolledException() {
    }

    public StudentNotEnrolledException(String msg) {
        super(msg);
    }

}
