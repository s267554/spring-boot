package it.polito.ai.virtuallabs.services.exceptions;

public class StudentNotFoundException extends VirtualLabsServiceException {
    public StudentNotFoundException() {
    }

    public StudentNotFoundException(String msg) {
        super(msg);
    }
}
