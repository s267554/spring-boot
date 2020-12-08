package it.polito.ai.virtuallabs.controllers;

import it.polito.ai.virtuallabs.services.exceptions.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class VirtualLabsExceptionHandler {

    @ResponseBody
    @ExceptionHandler({
            CourseNotFoundException.class,
            StudentNotFoundException.class,
            ProfessorNotFoundException.class,
            VirtualMachineNotFoundException.class,
            TeamNotFoundException.class,
            AssignmentNotFoundException.class
    })
    public ResponseEntity<String> handleNotFoundException(@NotNull VirtualLabsServiceException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler({
           CourseAlreadyExistsException.class
    })
    public ResponseEntity<String> handleConflictException(@NotNull VirtualLabsServiceException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ResponseBody
    @ExceptionHandler({
            CourseNotEnabledException.class,
            ProfessorNotAuthorizedException.class,
            StudentNotAuthorizedException.class
    })
    public ResponseEntity<String> handleForbiddenException(@NotNull VirtualLabsServiceException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

}
