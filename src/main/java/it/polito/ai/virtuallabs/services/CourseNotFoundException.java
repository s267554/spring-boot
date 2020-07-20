package it.polito.ai.virtuallabs.services;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CourseNotFoundException extends VirtualLabsServiceException {

    public CourseNotFoundException() {
    }

    public CourseNotFoundException(String msg) {
        super(msg);
    }

}
