package it.polito.ai.virtuallabs.controllers;

import it.polito.ai.virtuallabs.dtos.StudentDTO;
import it.polito.ai.virtuallabs.dtos.VirtualMachineDTO;
import it.polito.ai.virtuallabs.dtos.VirtualMachineModelDTO;
import it.polito.ai.virtuallabs.entities.VirtualMachineModel;
import it.polito.ai.virtuallabs.repositories.VirtualMachineModelRepository;
import it.polito.ai.virtuallabs.services.ImageService;
import it.polito.ai.virtuallabs.services.VirtualLabsService;
import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/api/vms")
public class VirtualMachineController {

    private final VirtualLabsService virtualLabsService;

    private final VirtualMachineModelRepository virtualMachineModelRepository;

    private final ImageService imageService;

    private final ModelMapper modelMapper;

    public VirtualMachineController(VirtualLabsService virtualLabsService, VirtualMachineModelRepository virtualMachineModelRepository, ImageService imageService, ModelMapper modelMapper) {
        this.virtualLabsService = virtualLabsService;
        this.virtualMachineModelRepository = virtualMachineModelRepository;
        this.imageService = imageService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    public List<StudentDTO> getOwners(@PathVariable(name = "id") Long id) {
        return virtualLabsService.getStudentsOfVM(id).stream()
                .map(ModelHelper::enrich)
                .collect(Collectors.toList());
    }

    @GetMapping("/models")
    public List<VirtualMachineModelDTO> getModels() {
        return virtualMachineModelRepository.findAll().stream()
                .map(m -> modelMapper.map(m, VirtualMachineModelDTO.class))
                .collect(Collectors.toList());
    }

    @PostMapping("/models")
    public VirtualMachineModelDTO addModel(@RequestParam("file") MultipartFile multipartImage,
                                           @RequestParam("name") @NotBlank String name) throws IOException {
        final VirtualMachineModel virtualMachineModel = new VirtualMachineModel();
        virtualMachineModel.setName(name);
        virtualMachineModel.setUrl(imageService.uploadImage(multipartImage));

        return modelMapper.map(
                virtualMachineModelRepository.save(virtualMachineModel),
                VirtualMachineModelDTO.class);
    }

}
