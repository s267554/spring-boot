package it.polito.ai.virtuallabs.controllers;

import it.polito.ai.virtuallabs.dtos.StudentDTO;
import it.polito.ai.virtuallabs.services.VirtualLabsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/api/vms")
public class VirtualMachineController {

    private final VirtualLabsService virtualLabsService;

    public VirtualMachineController(VirtualLabsService virtualLabsService) {
        this.virtualLabsService = virtualLabsService;
    }

    @GetMapping("/{id}")
    public List<StudentDTO> getOwners(@PathVariable(name = "id") Long id) {
        return virtualLabsService.getStudentsOfVM(id).stream()
                .map(ModelHelper::enrich)
                .collect(Collectors.toList());
    }

}
