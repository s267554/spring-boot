package it.polito.ai.virtuallabs.services.exceptions;

public class VirtualMachineNotFoundException extends VirtualLabsServiceException {
    public VirtualMachineNotFoundException() {
    }

    public VirtualMachineNotFoundException(String msg) {
        super(msg);
    }
}
