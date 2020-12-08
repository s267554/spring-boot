package it.polito.ai.virtuallabs.services.exceptions;

public class PaperNotFoundException extends VirtualLabsServiceException {
    public PaperNotFoundException() {
    }

    public PaperNotFoundException(String msg) {
        super(msg);
    }
}
