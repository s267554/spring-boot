package it.polito.ai.virtuallabs.services.exceptions;

public class CourseAlreadyExistsException extends VirtualLabsServiceException {

    public CourseAlreadyExistsException() {
    }

    public CourseAlreadyExistsException(String msg) {
        super(msg);
    }

}
