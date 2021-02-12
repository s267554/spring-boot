package it.polito.ai.virtuallabs.controllers;

import it.polito.ai.virtuallabs.dtos.PaperDTO;
import it.polito.ai.virtuallabs.dtos.PaperVersionDTO;
import it.polito.ai.virtuallabs.services.VirtualLabsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final VirtualLabsService virtualLabsService;

    public AssignmentController(VirtualLabsService virtualLabsService) {
        this.virtualLabsService = virtualLabsService;
    }

    @GetMapping("/{id}/papers")
    public List<PaperDTO> getPapers(@PathVariable(name = "id") Long id) {
        return virtualLabsService.getPapersOfAssignment(id).stream()
                .map(ModelHelper::enrich)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/papers/{studentId}/versions")
    public List<PaperVersionDTO> getPaperVersions(@PathVariable(name = "id") Long id,
                                                  @PathVariable(name = "studentId") String studentId) {
        return virtualLabsService.getPaperVersionsOfPaper(id, studentId).stream()
                .map(ModelHelper::enrich)
                .collect(Collectors.toList());
    }

    @PostMapping("/{id}/papers/create")
    public PaperVersionDTO addPaperVersion(@PathVariable(name = "id") Long id,
                                                 @RequestBody @Valid PaperVersionDTO paperVersionDTO) {
        return ModelHelper.enrich(virtualLabsService.addPaperVersion(id, paperVersionDTO));
    }

    @PutMapping("/{id}/papers/{studentId}")
    public PaperDTO updatePaper(@PathVariable(name = "id") Long id,
                                                  @PathVariable(name = "studentId") String studentId,
                                @RequestBody @Valid PaperDTO paperDTO) {
        return ModelHelper.enrich(virtualLabsService.updatePaper(id, studentId, paperDTO));
    }

    @GetMapping("/{id}/read")
    public PaperDTO readPaper(@PathVariable(name = "id") Long id) {
        return virtualLabsService.readPaper(id);
    }

}
