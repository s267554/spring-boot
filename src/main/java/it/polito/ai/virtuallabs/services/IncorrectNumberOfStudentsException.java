package it.polito.ai.virtuallabs.services;

public class IncorrectNumberOfStudentsException extends VirtualLabsServiceException {

    public IncorrectNumberOfStudentsException() {
    }

    public IncorrectNumberOfStudentsException(String msg) {
        super(msg);
    }

}
