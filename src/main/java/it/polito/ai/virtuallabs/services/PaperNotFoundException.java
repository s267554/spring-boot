package it.polito.ai.virtuallabs.services;

public class PaperNotFoundException extends VirtualLabsServiceException {
    public PaperNotFoundException() {
    }

    public PaperNotFoundException(String msg) {
        super(msg);
    }
}
