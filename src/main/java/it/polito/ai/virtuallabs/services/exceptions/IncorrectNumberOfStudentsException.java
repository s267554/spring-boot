package it.polito.ai.virtuallabs.services.exceptions;

public class IncorrectNumberOfStudentsException extends VirtualLabsServiceException {

    public IncorrectNumberOfStudentsException() {
    }

    public IncorrectNumberOfStudentsException(String msg) {
        super(msg);
    }

}
