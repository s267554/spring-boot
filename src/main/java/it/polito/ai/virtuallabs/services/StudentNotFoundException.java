package it.polito.ai.virtuallabs.services;

public class StudentNotFoundException extends VirtualLabsServiceException {
    public StudentNotFoundException() {
    }

    public StudentNotFoundException(String msg) {
        super(msg);
    }
}
