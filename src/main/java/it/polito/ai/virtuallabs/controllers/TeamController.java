package it.polito.ai.virtuallabs.controllers;

import it.polito.ai.virtuallabs.dtos.StudentDTO;
import it.polito.ai.virtuallabs.dtos.TeamDTO;
import it.polito.ai.virtuallabs.dtos.TeamEmbeddedDTO;
import it.polito.ai.virtuallabs.dtos.VirtualMachineDTO;
import it.polito.ai.virtuallabs.models.ProposeTeamRequest;
import it.polito.ai.virtuallabs.services.NotificationService;
import it.polito.ai.virtuallabs.services.VirtualLabsService;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/api/courses/{courseName}")
public class TeamController {

    private final NotificationService notificationService;

    private final VirtualLabsService virtualLabsService;

    public TeamController(VirtualLabsService virtualLabsService, NotificationService notificationService) {
        this.virtualLabsService = virtualLabsService;
        this.notificationService = notificationService;
    }

    // the one with the request param
    @GetMapping("/teams")
    public List<?> getTeams(@PathVariable(name = "courseName") @NotBlank String courseName,
                                              @RequestParam(name = "studentId", required = false) String studentId) {
        if (studentId == null || studentId.isEmpty()) {
            final List<TeamDTO> teams = virtualLabsService.getTeamsOfCourse(courseName);
            return teams.stream()
                    .map(ModelHelper::enrich)
                    .collect(Collectors.toList());
        }
        final List<TeamEmbeddedDTO> teams = virtualLabsService.getTeamsOfCourseByStudentId(courseName, studentId);
        return teams.stream()
                .map(ModelHelper::enrich)
                .collect(Collectors.toList());
    }

    @GetMapping("/teams/{teamName}/students")
    public List<StudentDTO> getStudentsByTeam(@PathVariable(name = "courseName") @NotBlank String courseName,
                       @PathVariable(name = "teamName") @NotBlank String teamName) {
        return virtualLabsService.getStudentsOfCourseByTeamId(courseName, teamName).stream()
                .map(ModelHelper::enrich)
                .collect(Collectors.toList());
    }

    @PutMapping("/teams/{teamName}")
    public void update(@PathVariable(name = "courseName") @NotBlank String courseName,
                       @PathVariable(name = "teamName") @NotBlank String teamName,
                       @RequestBody @Valid TeamDTO teamDTO) {
        virtualLabsService.updateTeam(courseName, teamName, teamDTO);
    }

    @GetMapping("/teams/{teamName}/confirmTeam")
    public void confirmTeam(@PathVariable(name = "courseName") @NotBlank String courseName,
                            @PathVariable(name = "teamName") @NotBlank String teamName) {

        notificationService.confirmTeam(virtualLabsService.getTokenByCourseAndTeam(courseName, teamName));
    }

    @GetMapping("/teams/{teamName}/rejectTeam")
    public void rejectTeam(@PathVariable(name = "courseName") @NotBlank String courseName,
                            @PathVariable(name = "teamName") @NotBlank String teamName) {

        notificationService.rejectTeam(virtualLabsService.getTokenByCourseAndTeam(courseName, teamName));
    }

    @GetMapping("/teams/{teamName}/deleteTeam")
    public void deleteTeam(@PathVariable(name = "courseName") @NotBlank String courseName,
                           @PathVariable(name = "teamName") @NotBlank String teamName) {

        notificationService.deleteTeam(virtualLabsService.getTokenByCourseAndTeam(courseName, teamName));
    }

    @PostMapping("/proposeTeam")
    public TeamDTO proposeTeam(@PathVariable(name = "courseName") @NotBlank String courseName,
                               @RequestBody @Valid @NotNull ProposeTeamRequest request) {

        final List<String> ids = request.getIds();

        final TeamDTO t = virtualLabsService.proposeTeam(courseName, request.getName(), ids, request.getTimeout());

        notificationService.notifyTeam(t, ids, request.getTimeout());

        return ModelHelper.enrich(t);

    }

    @GetMapping("/teams/{teamName}/vms")
    public List<VirtualMachineDTO> getVMsOfTeam(@PathVariable(name = "courseName") @NotBlank String courseName,
                                                @PathVariable(name = "teamName") @NotBlank String teamName) {
        final List<VirtualMachineDTO> vms = virtualLabsService.getVMsOfTeam(courseName, teamName);
        return vms.stream()
                .map(ModelHelper::enrich)
                .collect(Collectors.toList());
    }

    @PostMapping("/teams/{teamName}/createVM")
    public VirtualMachineDTO createVM(@PathVariable(name = "courseName") @NotBlank String courseName,
                                      @PathVariable(name = "teamName") @NotBlank String teamName,
                                      @RequestBody @NotNull VirtualMachineDTO virtualMachineDTO) {

        final VirtualMachineDTO v = virtualLabsService.addVMToTeam(courseName, teamName, virtualMachineDTO);

        return ModelHelper.enrich(v);

    }

    @PutMapping("/teams/{teamName}/vms/{id}")
    public VirtualMachineDTO updateVM(@PathVariable(name = "courseName") @NotBlank String courseName,
                                      @PathVariable(name = "teamName") @NotBlank String teamName,
                                      @PathVariable(name = "id") Long id,
                                      @RequestBody @NotNull VirtualMachineDTO virtualMachineDTO) {

        final VirtualMachineDTO v = virtualLabsService.updateVM(courseName, teamName, virtualMachineDTO, id);

        return ModelHelper.enrich(v);

    }

    @GetMapping("/notInTeam")
    public List<StudentDTO> getStudentsNotInTeam(@PathVariable(name = "courseName") @NotBlank String courseName) {
        return virtualLabsService.getStudentsNotInTeam(courseName).stream()
                .map(ModelHelper::enrich)
                .collect(Collectors.toList());
    }

}
