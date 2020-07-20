package it.polito.ai.virtuallabs.services;

public class VirtualMachineActiveException extends VirtualLabsServiceException {
    public VirtualMachineActiveException() {
    }

    public VirtualMachineActiveException(String msg) {
        super(msg);
    }
}
