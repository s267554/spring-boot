package it.polito.ai.virtuallabs.services;

public class PaperIsNotEnabledException extends VirtualLabsServiceException {
    public PaperIsNotEnabledException() {
    }

    public PaperIsNotEnabledException(String msg) {
        super(msg);
    }
}
