package it.polito.ai.virtuallabs.services;

public class VirtualMachineNotFoundException extends VirtualLabsServiceException {
    public VirtualMachineNotFoundException() {
    }

    public VirtualMachineNotFoundException(String msg) {
        super(msg);
    }
}
