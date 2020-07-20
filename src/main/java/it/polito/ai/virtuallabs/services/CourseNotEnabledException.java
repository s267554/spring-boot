package it.polito.ai.virtuallabs.services;

public class CourseNotEnabledException extends VirtualLabsServiceException {

    public CourseNotEnabledException() {
    }

    public CourseNotEnabledException(String msg) {
        super(msg);
    }

}
