package it.polito.ai.virtuallabs.services;

import it.polito.ai.virtuallabs.dtos.*;

import java.io.Reader;
import java.util.List;

public interface VirtualLabsService {

    CourseDTO createCourse(CourseDTO courseDTO);

    void updateCourse(CourseDTO courseDTO);

    void deleteCourse(String courseName);

    void enableCourse(String courseName);

    void disableCourse(String courseName);

    List<StudentDTO> getStudentsOfCourse(String courseName);

    List<StudentDTO> getStudentsNotInCourse(String courseName);

    List<CourseDTO> getCoursesOfUser();

    void addStudentToCourse(String courseName, String username);

    void removeStudentsFromCourse(String courseName, List<String> usernames);

    List<TeamDTO> getTeamsOfCourse(String courseName);

    List<VirtualMachineDTO> getVMsOfTeam(String courseName, String teamName);

    List<StudentDTO> getStudentsOfVM(Long id);

    void updateTeam(String courseName, String teamName, TeamDTO teamDTO);

    List<AssignmentDTO> getAssignmentsOfCourse(String courseName);

    List<PaperDTO> getPapersOfAssignment(Long assignmentId);

    List<PaperVersionDTO> getPaperVersionsOfPaper(Long assignmentId, String username);

    // Assignments

    AssignmentDTO addAssignmentToCourse(String courseName, AssignmentDTO assignmentDTO);

    // Paper

    void enablePaper(Long assignmentId, String studentId);

    void disablePaper(Long assignmentId, String studentId);

    void addPaper(Long assignmentId, List<String> studentIds);

    PaperDTO updatePaper(Long assignmentId, String studentId, PaperDTO paperDTO);

    TeamEmbeddedDTO proposeTeam(String courseName, String teamName, List<String> studentIds, Long timeout);

    List<StudentDTO> getStudentsNotInTeam(String courseName);

    List<TeamEmbeddedDTO> getTeamsOfCourseByStudentId(String courseName, String studentId);

    List<StudentDTO> getStudentsOfCourseByTeamId(String courseName, String teamName);

    String getTokenByCourseAndTeam(String courseName, String teamName);
//
//    TeamDTO getTeam(String courseName);

    // TODO: Student

//    List<ProfessorDTO> getProfessorsOfCourse(String courseName);
//
//    // Professors - Courses
//
//    void addProfessorToCourse(String courseName, String professorId);
//
//    List<VirtualMachineDTO> getVMsOfStudent(String studentId);
//
//    // Teams
//
//    void enableTeam(String courseName, String teamName);
//
//    void evictTeam(String courseName, String teamName);
//
//    List<StudentDTO> getStudentsOfTeam(String courseName, String teamName);
//
//    void updateVMModelOfTeam(String courseName, String teamName, VirtualMachineModelDTO vmmodel);
//
    // VMs

    VirtualMachineDTO addVMToTeam(String courseName, String teamName, VirtualMachineDTO vm);

    void addStudentToVM(Long id, String studentId);

    VirtualMachineDTO updateVM(String courseName, String teamName, VirtualMachineDTO vm, Long id);

    void deleteVM(String courseName, String teamName, Long id);

    // Paper Versions

    PaperVersionDTO addPaperVersion(Long assignmentId, PaperVersionDTO paperVersionDTO);


    PaperDTO readPaper(Long assignmentId);

    List<StudentDTO> addAndEnroll(Reader r, String courseName);
}
