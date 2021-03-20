package it.polito.ai.virtuallabs.controllers;

import it.polito.ai.virtuallabs.dtos.AssignmentDTO;
import it.polito.ai.virtuallabs.dtos.CourseDTO;
import it.polito.ai.virtuallabs.dtos.StudentDTO;
import it.polito.ai.virtuallabs.services.VirtualLabsService;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final VirtualLabsService virtualLabsService;

    public CourseController(VirtualLabsService virtualLabsService) {
        this.virtualLabsService = virtualLabsService;
    }

    @GetMapping({"", "/"})
    public List<CourseDTO> getAll() {
        return virtualLabsService.getCoursesOfUser().stream()
                .map(ModelHelper::enrich)
                .collect(Collectors.toList());
    }

    @PostMapping({"", "/"})
    public CourseDTO create(@Valid @RequestBody CourseDTO courseDTO) {
        final CourseDTO c = virtualLabsService.createCourse(courseDTO);
        return ModelHelper.enrich(c);
    }

    @DeleteMapping("/{courseName}")
    public void delete(@PathVariable(name = "courseName") @NotBlank String courseName) {
        virtualLabsService.deleteCourse(courseName);
    }

    @PutMapping({"", "/"})
    public void update(@Valid @RequestBody CourseDTO courseDTO) {
        virtualLabsService.updateCourse(courseDTO);
    }

    @PostMapping("/{courseName}/enable")
    public void enable(@PathVariable(name = "courseName") @NotBlank String courseName) {
        virtualLabsService.enableCourse(courseName);
    }

    @PostMapping("/{courseName}/disable")
    public void disable(@PathVariable(name = "courseName") @NotBlank String courseName) {
        virtualLabsService.disableCourse(courseName);
    }

    @GetMapping("/{courseName}/enrolled")
    public List<StudentDTO> enrolled(@PathVariable(name = "courseName") @NotBlank String courseName) {
        final List<StudentDTO> studentDTOS = virtualLabsService.getStudentsOfCourse(courseName);
        return studentDTOS.stream()
                .map(ModelHelper::enrich)
                .collect(Collectors.toList());
    }

    @PostMapping("/{courseName}/enrollOne")
    public void enrollOne(@PathVariable(name = "courseName") @NotBlank String courseName,
                          @RequestBody @Valid @NotNull StudentDTO studentDTO) {
        virtualLabsService.addStudentToCourse(courseName, studentDTO.getId());

        // aggiungo paper ancora consegnabili
        final Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        virtualLabsService.getAssignmentsOfCourse(courseName)
                .stream().filter(assignmentDTO -> assignmentDTO.getExpiryDate().before(now))
                .map(AssignmentDTO::getId)
                .forEach(aLong -> virtualLabsService.addPaper(aLong, Collections.singletonList(studentDTO.getId())));
    }

    @PostMapping("/{courseName}/enrollMany")
    public List<StudentDTO> enrollStudents(
            @PathVariable(name = "courseName") String courseName,
            @RequestParam("file") MultipartFile file) throws IOException {
        if ("text/csv".equals(file.getContentType())) {
            final Reader r = new InputStreamReader(file.getInputStream());
            return virtualLabsService.addAndEnroll(r, courseName);
        } else {
            throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, file.getContentType());
        }
    }

    @PostMapping("/{courseName}/dropOutAll")
    public void removeStudentsFromCourse(@PathVariable(name = "courseName") @NotBlank String courseName,
                                         @RequestBody @NotNull @Valid List<StudentDTO> studentDTOS) {
        final List<String> ids = studentDTOS.stream()
                .map(StudentDTO::getId)
                .collect(Collectors.toList());
        virtualLabsService.removeStudentsFromCourse(courseName, ids);
    }

    @GetMapping("/{courseName}/notEnrolled")
    public List<StudentDTO> getStudentsNotEnrolled(@PathVariable(name = "courseName") @NotBlank String courseName) {
        final List<StudentDTO> students = virtualLabsService.getStudentsNotInCourse(courseName);
        return students.stream()
                .map(ModelHelper::enrich)
                .collect(Collectors.toList());
    }

    @GetMapping("/{courseName}/assignments")
    public List<AssignmentDTO> getAssignments(@PathVariable(name = "courseName") @NotBlank String courseName) {
        final List<AssignmentDTO> assignmentDTOS = virtualLabsService.getAssignmentsOfCourse(courseName);
        return assignmentDTOS.stream()
                .map(ModelHelper::enrich)
                .collect(Collectors.toList());
    }

    @PostMapping("/{courseName}/assignments")
    public AssignmentDTO createAssignment(@PathVariable(name = "courseName") @NotBlank String courseName,
                                          @Valid @RequestBody AssignmentDTO assignmentDTO) {
        final AssignmentDTO a = virtualLabsService.addAssignmentToCourse(courseName, assignmentDTO);

        List<String> studentIds = virtualLabsService.getStudentsOfCourse(courseName)
                .stream().map(StudentDTO::getId).collect(Collectors.toList());
        virtualLabsService.addPaper(a.getId(), studentIds);

        return ModelHelper.enrich(a);
    }

}
