package it.polito.ai.virtuallabs.services.exceptions;

public class VirtualLabsServiceException extends RuntimeException {

    public VirtualLabsServiceException() {
        super();
    }

    public VirtualLabsServiceException(String msg) {
        super(msg);
    }

}
