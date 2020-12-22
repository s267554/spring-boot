package it.polito.ai.virtuallabs.services;

import it.polito.ai.virtuallabs.dtos.*;
import it.polito.ai.virtuallabs.entities.*;
import it.polito.ai.virtuallabs.repositories.*;
import it.polito.ai.virtuallabs.security.IAuthenticationFacade;
import it.polito.ai.virtuallabs.services.exceptions.*;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class VirtualLabsServiceImpl implements VirtualLabsService {

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private VirtualMachineRepository virtualMachineRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TeamTokenRepository teamTokenRepository;

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CourseDTO createCourse(@NotNull CourseDTO courseDTO) {

        // Check if a course with the same name already exists
        final String name = courseDTO.getName();
        if (courseRepository.existsById(name)) {
            throw new CourseAlreadyExistsException("Course " + name + " already exists");
        }

        // Load current professor who will be the owner of the course.
        final Professor professor = loadCurrentProfessor();

        final Course course = modelMapper.map(courseDTO, Course.class);
        course.addProfessor(professor);

        return modelMapper.map(courseRepository.save(course), CourseDTO.class);

    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateCourse(@NotNull CourseDTO courseDTO) {

        final String name = courseDTO.getName();

        // If nothing exception is thrown, the current professor is authorized to
        // modify the course
        final Course course = loadCourseIfProfessorIsAuthorized(name);

        modelMapper.map(courseDTO, course);

        courseRepository.save(course);

    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteCourse(String courseName) {
        // If nothing exception is thrown, the current professor is authorized to
        // modify the course
        final Course course = loadCourseIfProfessorIsAuthorized(courseName);

        courseRepository.delete(course);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void enableCourse(String courseName) {
        // If nothing exception is thrown, the current professor is authorized to
        // modify the course
        final Course course = loadCourseIfProfessorIsAuthorized(courseName);
        course.setEnabled(true);

        courseRepository.save(course);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void disableCourse(String courseName) {
        // If nothing exception is thrown, the current professor is authorized to
        // modify the course
        final Course course = loadCourseIfProfessorIsAuthorized(courseName);
        course.setEnabled(false);

        courseRepository.save(course);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<StudentDTO> getStudentsOfCourse(String courseName) {
        // If nothing exception is thrown, the current professor is authorized to
        // fetch students of the course
        final Course course = loadCourseIfProfessorIsAuthorized(courseName);

        List<StudentDTO> studentDTOS = mapToStudentDTOs(course.getStudents());
        for (StudentDTO s : studentDTOS) {
            List<Team> teams = teamRepository.findByCourseAndStudent(courseName, s.getEmail());
            if (teams.size() > 0)
                s.setTeamName(teams.get(0).getKey().getName());
        }
        return studentDTOS;

    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<StudentDTO> getStudentsNotInCourse(String courseName) {
        // Check if current professor is authorized to access
        // the course
        loadCourseIfProfessorIsAuthorized(courseName);

        return mapToStudentDTOs(studentRepository.findStudentsNotInCourse(courseName));

    }

    @Override
    public List<CourseDTO> getCoursesOfUser() {
        final List<Course> courses = isCurrentUserIsAdmin() ? loadCurrentProfessor().getCourses()
                : loadCurrentStudent().getCourses();
        return mapToCourseDTOs(courses);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void addStudentToCourse(String courseName, String username) {

        final Course course = loadCourseIfProfessorIsAuthorized(courseName);
        final Student student = loadStudent(username);

        course.addStudent(student);

        courseRepository.save(course);

    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void removeStudentsFromCourse(String courseName, @NotNull List<String> usernames) {

        final Course course = loadCourseIfProfessorIsAuthorized(courseName);

        usernames.stream()
                .map(this::loadStudent)
                .forEach(course::removeStudent);

        courseRepository.save(course);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<TeamDTO> getTeamsOfCourse(String courseName) {
        final Course course = loadCourseIfProfessorIsAuthorized(courseName);

        return course.getTeams().stream()
                .map(t -> modelMapper.map(t, TeamDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<VirtualMachineDTO> getVMsOfTeam(String courseName, String teamName) {
        final Team team = loadTeamIfProfessorIsAuthorized(courseName, teamName);

        return team.getVirtualMachines().stream()
                .map((vm) -> modelMapper.map(vm, VirtualMachineDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<StudentDTO> getStudentsOfVM(Long id) {
        final VirtualMachine vm = loadVMIfProfessorIsAuthorized(id);
        return mapToStudentDTOs(vm.getOwners());
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updateTeam(String courseName, String teamName, @NotNull TeamDTO teamDTO) {
        final Team team = loadTeamIfProfessorIsAuthorized(courseName, teamName);

        // Fetch all vms in order to calculate the current resources used by the team.
        final List<VirtualMachine> vms = team.getVirtualMachines();

        int totVcpu = vms.stream().map(VirtualMachine::getVcpu).reduce(0, Integer::sum);
        double space = vms.stream().map(VirtualMachine::getSpace).reduce(0.0, Double::sum);
        double ram = vms.stream().map(VirtualMachine::getRam).reduce(0.0, Double::sum);

        // If the new resources are less than current used resources, the team cannot be updated
        if (totVcpu > teamDTO.getVcpu() || space > teamDTO.getSpace() || ram > teamDTO.getRam()) {
            throw new TeamResourcesExceededException("The new resources cannot be less than current resources used");
        }

        modelMapper.map(teamDTO, team);

        teamRepository.save(team);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<AssignmentDTO> getAssignmentsOfCourse(String courseName) {
        final Course course = loadCourseIfProfessorIsAuthorized(courseName);

        return course.getAssignments().stream()
                .map((a) -> modelMapper.map(a, AssignmentDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<PaperDTO> getPapersOfAssignment(Long assignmentId) {
        final Assignment assignment = loadAssignmentIfProfessorIsAuthorized(assignmentId);

        return assignment.getPapers().stream()
                .map((p) -> modelMapper.map(p, PaperDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<PaperVersionDTO> getPaperVersionsOfPaper(Long assignmentId, String username) {
        final Paper paper = loadPaperIfProfessorIsAuthorized(assignmentId, username);

        return paper.getVersions().stream()
                .map((v) -> modelMapper.map(v, PaperVersionDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public AssignmentDTO addAssignmentToCourse(String courseName, AssignmentDTO assignmentDTO) {
        final Course course = loadCourseIfProfessorIsAuthorized(courseName);

        final Timestamp creationDate = Timestamp.valueOf(LocalDateTime.now());
        final Assignment assignment = modelMapper.map(assignmentDTO, Assignment.class);
        assignment.setCreationDate(creationDate);
        assignment.setCourse(course);

        return modelMapper.map(assignmentRepository.save(assignment), AssignmentDTO.class);

    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void enablePaper(Long assignmentId, String studentId) {
        final Paper paper = loadPaperIfProfessorIsAuthorized(assignmentId, studentId);
        paper.setEnabled(true);

        paperRepository.save(paper);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void disablePaper(Long assignmentId, String studentId) {
        final Paper paper = loadPaperIfProfessorIsAuthorized(assignmentId, studentId);
        paper.setEnabled(false);

        paperRepository.save(paper);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void updatePaper(Long assignmentId, String studentId, PaperDTO paperDTO) {
        final Paper paper = loadPaperIfProfessorIsAuthorized(assignmentId, studentId);

        modelMapper.map(paperDTO, paper);

        paperRepository.save(paper);
    }

    @Override
    @PreAuthorize("hasRole('ROLE_USER')")
    public TeamDTO proposeTeam(String courseName, String teamName, List<String> studentIds) {

        final Team.Key key = new Team.Key(courseName, teamName);
        if (teamRepository.existsById(key)) {
            throw new TeamAlreadyExistsException("Team " + teamName + " of course " + courseName + " already exist");
        }

        final Course course = loadCourse(courseName);

        if (!course.isEnabled()) {
            throw new CourseNotEnabledException("Course " + courseName + " is not enabled");
        }

        final Student student = loadCurrentStudent();

        if (!course.getStudents().contains(student)) {
            throw new StudentNotEnrolledException("Student " + student.getId()
                    + " is not enrolled into course " + courseName);
        }

        final List<String> ids = studentIds.stream()
                .distinct()
                .filter((id) -> !id.equals(student.getId()) || !id.equals(student.getEmail()))
                .collect(Collectors.toList());

        if (ids.size() > course.getMax() || ids.size() < course.getMin()) {
            throw new IncorrectNumberOfStudentsException("Team " + teamName + " must have max "
                    + course.getMax() + " and min " + course.getMin() + " members");
        }

        final Team team = new Team();
        team.addMember(student);
        team.setCourse(course);
        team.setKey(key);
        team.setCreator(student.getId());

        final List<Student> students = ids.stream()
                .map(this::loadStudent)
                .collect(Collectors.toList());

        final List<List<Student>> teams = course.getTeams().stream()
                .filter(Team::isEnabled)
                .map(Team::getMembers)
                .collect(Collectors.toList());

        students.forEach((s) -> {

            if (!course.getStudents().contains(s)) {
                throw new StudentNotEnrolledException("Student " + s.getId()
                        + " is not enrolled into course " + courseName);
            }

            if (teams.stream().anyMatch((t) -> t.contains(s))) {
                throw new StudentAlreadyHasATeamException("Student " + s.getId() + " already has a team in the course");
            }

            team.addMember(s);

        });

        return modelMapper.map(teamRepository.save(team), TeamDTO.class);

    }

    @Override
    public List<StudentDTO> getStudentsNotInTeam(String courseName) {
        final Course course = loadCourse(courseName);

        if (isCurrentUserIsAdmin()) {

            final Professor professor = loadCurrentProfessor();
            if (!course.getProfessors().contains(professor)) {
                throw new ProfessorNotAuthorizedException("Professor " + professor.getId()
                        + " is not authorized to access teh course " + courseName);
            }

        } else {

            final Student student = loadCurrentStudent();
            if (!course.getStudents().contains(student)) {
                throw new StudentNotAuthorizedException("Student " + student.getId()
                        + " is not authorized to access the course " + courseName);
            }

        }

        final List<Student> students = studentRepository.findStudentsNotInCourse(courseName);

        return mapToStudentDTOs(students);
    }

    @Override
    // only student id=studentId is authorized to proceed
    // @PreAuthorize("#studentId == authenticationFacade.getAuthentication().getName()")
    public List<TeamDTO> getTeamsOfCourseByStudentId(String courseName, String studentId) {
        // student must be enrolled within the course
        final Course course = loadCourse(courseName);

        final Student student = loadCurrentStudent();

        if (!course.getStudents().contains(student)) {
            throw new StudentNotEnrolledException("Student " + student.getId()
                    + " is not enrolled into course " + courseName);
        }

        // return enabled team else any other temp team that includes such student

        // must be better way to achieve all of this
        Optional<Team> ret = course.getTeams().stream()
                .filter((t) -> t.getMembers().contains(student))
                .filter(Team::isEnabled)
                .findAny();

        if (ret.isPresent())
            return Collections.singletonList(modelMapper.map(ret, TeamDTO.class));

        return course.getTeams().stream()
                .filter((t) -> t.getMembers().contains(student))
                .map(t -> modelMapper.map(t, TeamDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentDTO> getStudentsOfCourseByTeamId(String courseName, String teamName) {
        // no further authorization needed?

        final Course course = loadCourse(courseName);

        final Student student = loadCurrentStudent();

        if (!course.getStudents().contains(student)) {
            throw new StudentNotEnrolledException("Student " + student.getId()
                    + " is not enrolled into course " + courseName);
        }

        final Team team = loadTeam(courseName, teamName);

        // fa schifo
        return team.getMembers().stream()
                .map(s -> modelMapper.map(s, StudentDTO.class))
                .peek(s -> {
                    if (team.isEnabled())
                        s.setTeamName(teamName);
                    if (teamTokenRepository.findAllByTeamIdAndStudent(new Team.Key(courseName, teamName), loadStudent(s.getId()))
                            .isEmpty())
                        s.setTempTeamName(teamName);
                })
                .collect(Collectors.toList());
    }

    // an utility function to retrieve tokenid and call accept/reject team withous checking email
    @Override
    public String getTokenByCourseAndTeam(String courseName, String teamName) {
        final Student student = loadCurrentStudent();
        final Course course = loadCourse(courseName);
        if (!course.getStudents().contains(student)) {
            throw new StudentNotAuthorizedException("Student " + student.getId()
                    + " cannot access the course " + courseName);
        }
        final Team team = loadTeam(courseName, teamName);
        if (!team.getMembers().contains(student)) {
            throw new StudentNotAuthorizedException("Student " + student.getId()
                    + " cannot access the team " + teamName);
        }
        return teamTokenRepository.findByTeamIdAndStudent(team.getKey(), student).getId();
    }

//    @Override
//    @PreAuthorize("hasRole('ROLE_USER')")
//    public TeamDTO getTeam(String courseName) {
//        final Student student = loadCurrentStudent();
//        final Course course = loadCourse(courseName);
//        if (!course.getStudents().contains(student)) {
//            throw new StudentNotAuthorizedException("Student " + student.getId()
//                    + " cannot access the course " + courseName);
//        }
//       final Team team = teamRepository.findByCourseAndStudent(courseName, student.getId())
//               .orElseThrow(() -> new TeamNotFoundException("Team for student " + student.getId()
//                       + " in the course " + courseName + " not found"));
//        return modelMapper.map(team, TeamDTO.class);
//    }

    /**
     * @return
     */
    private boolean isCurrentUserIsAdmin() {
        return authenticationFacade.getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch((r) -> r.equals("ROLE_ADMIN"));
    }

    /**
     * @param assignmentId
     * @param username
     * @return
     * @throws PaperNotFoundException
     * @throws ProfessorNotFoundException
     * @throws ProfessorNotAuthorizedException
     */
    private @NotNull Paper loadPaperIfProfessorIsAuthorized(Long assignmentId, String username)
            throws PaperNotFoundException, ProfessorNotFoundException, ProfessorNotAuthorizedException {
        final Professor professor = loadCurrentProfessor();
        final Paper paper = loadPaper(assignmentId, username);
        if (paper.getAssignment().getCourse().getProfessors().contains(professor)) {
            throw new ProfessorNotAuthorizedException("Professor " + professor.getId()
                    + " is not authorized to access paper");
        }
        return paper;
    }

    /**
     * @param assignmentId
     * @param username
     * @return
     * @throws PaperNotFoundException
     */
    private Paper loadPaper(Long assignmentId, String username) throws PaperNotFoundException {
        return paperRepository.findById(new Paper.Key(assignmentId, username))
                .orElseThrow(() -> new PaperNotFoundException("Paper " + assignmentId +
                        " of student " + username + " not found"));
    }

    /**
     * @param id
     * @return
     * @throws AssignmentNotFoundException
     * @throws ProfessorNotAuthorizedException
     */
    private @NotNull Assignment loadAssignmentIfProfessorIsAuthorized(Long id)
            throws AssignmentNotFoundException, ProfessorNotFoundException, ProfessorNotAuthorizedException {
        final Professor professor = loadCurrentProfessor();
        final Assignment assignment = loadAssignment(id);
        if (assignment.getCourse().getProfessors().contains(professor)) {
            throw new ProfessorNotAuthorizedException("Professor " + professor.getId() +
                    " is not authorized to access the assignment " + id);
        }
        return assignment;
    }

    /**
     * @param id
     * @return
     * @throws AssignmentNotFoundException
     */
    private Assignment loadAssignment(Long id) throws AssignmentNotFoundException {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new AssignmentNotFoundException("Assignment " + id + " doesn't exist"));
    }

    /**
     * @param id
     * @return
     * @throws VirtualMachineNotFoundException
     * @throws ProfessorNotAuthorizedException
     */
    private @NotNull VirtualMachine loadVMIfProfessorIsAuthorized(Long id)
            throws VirtualMachineNotFoundException, ProfessorNotAuthorizedException {
        final Professor professor = loadCurrentProfessor();
        final VirtualMachine vm = loadVM(id);
        if (!vm.getTeam().getCourse().getProfessors().contains(professor)) {
            throw new ProfessorNotAuthorizedException("Professor " + professor.getId()
                    + " is not authorized to access vm " + id);
        }
        return vm;
    }

    /**
     * @param id
     * @return
     * @throws VirtualMachineNotFoundException
     */
    private VirtualMachine loadVM(Long id) throws VirtualMachineNotFoundException {
        return virtualMachineRepository.findById(id)
                .orElseThrow(() -> new VirtualMachineNotFoundException("Virtual machine " + id + " not found"));
    }

    /**
     * Method used to load a {@link Team} from local db only if the current logged in
     * {@link Professor} is authorized to do that. Throws a {@link TeamNotFoundException}
     * if the {@link Team} doesn't exist or a {@link ProfessorNotAuthorizedException} if
     * the {@link Professor} is not one of the owner of the {@link Course}.
     *
     * @param courseName identify the {@link Course} of the {@link Team}
     * @param teamName   the name of the {@link Team}
     * @return a {@link Team} matching both courseName and teamName
     * @throws TeamNotFoundException           if the {@link Team} is not present in the db
     * @throws ProfessorNotAuthorizedException if the current {@link Professor} is not authorized
     */
    private @NotNull Team loadTeamIfProfessorIsAuthorized(String courseName, String teamName)
            throws TeamNotFoundException, ProfessorNotAuthorizedException {
        final Professor professor = loadCurrentProfessor();
        final Team team = loadTeam(courseName, teamName);
        if (!team.getCourse().getProfessors().contains(professor)) {
            throw new ProfessorNotAuthorizedException("Professor " + professor.getId()
                    + " is not authorized to access the team " + teamName + " of course " + courseName);
        }
        return team;
    }

    /**
     * Utility method to load a {@link Team} from local db. The courseName and the
     * team name are both used to identify the {@link Team}.
     *
     * @param courseName identify the {@link Course} of the {@link Team}
     * @param teamName   the name of the {@link Team}
     * @return a {@link Team} matching both courseName and teamName
     * @throws TeamNotFoundException if the {@link Team} is not present in the db
     */
    private Team loadTeam(String courseName, String teamName) throws TeamNotFoundException {
        return teamRepository.findById(new Team.Key(courseName, teamName))
                .orElseThrow(() -> new TeamNotFoundException("Team " + teamName
                        + " of course " + courseName + " not found"));
    }

    /**
     * Utility method used to transform (map) a list of {@link Student}
     * to a list of {@link StudentDTO}
     *
     * @param students a list of {@link Student} which will be mapped to {@link StudentDTO}
     * @return a list of {@link StudentDTO}
     */
    private List<StudentDTO> mapToStudentDTOs(@NotNull List<Student> students) {
        return students.stream()
                .map((s) -> modelMapper.map(s, StudentDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Utility method used to transform (map) a list of {@link Course}
     * to a list of {@link CourseDTO}
     *
     * @param courses a list of {@link Course} which will be mapped to {@link CourseDTO}
     * @return a list of {@link CourseDTO}
     */
    private List<CourseDTO> mapToCourseDTOs(@NotNull List<Course> courses) {
        return courses.stream()
                .map((c) -> modelMapper.map(c, CourseDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Utility method used to load the current student from db. Throws a
     * {@link StudentNotFoundException} if the current user is not a student or
     * the student is not found into the db.
     *
     * @return the current logged in Student
     * @throws StudentNotFoundException if the current logged in user is not a Student
     */
    private Student loadCurrentStudent() throws StudentNotFoundException {
        final String username = authenticationFacade.getAuthentication().getName();
        return loadStudent(username);
    }

    /**
     * Utility method used to load a Student with a matching username from db.
     * Throws a StudentNotFoundException if student is not found.
     *
     * @param username used to find the Student
     * @return a Student with a matching username
     * @throws StudentNotFoundException if a student with a matching username doesn't exist
     */
    private Student loadStudent(String username) throws StudentNotFoundException {
        return studentRepository.findByUsername(username)
                .orElseThrow(() -> new StudentNotFoundException("Student " + username + " not found"));
    }

    /**
     * Utility method used to load a Professor by username.
     *
     * @param username used to identify a professor. Could be the email or the id.
     * @return the Professor with the matching username.
     * @throws ProfessorNotFoundException if Professor is not found locally.
     */
    private Professor loadProfessor(String username) throws ProfessorNotFoundException {
        return professorRepository.findByUsername(username)
                .orElseThrow(() -> new ProfessorNotFoundException("Professor " + username + " not found"));
    }

    /**
     * Utility method used to load the current professor. The username is retrieved
     * from the current logged in user using an instance of the IAuthenticationFacade.
     *
     * @return the current Professor
     * @throws ProfessorNotFoundException if Professor is not found locally.
     */
    private Professor loadCurrentProfessor() throws ProfessorNotFoundException {
        final String username = authenticationFacade.getAuthentication().getName();
        return loadProfessor(username);
    }

    /**
     * Utility method used to load a Course from db. If a course with a matching name
     * is not found a CourseNotFoundException is thrown.
     *
     * @param courseName the name of the course to load to.
     * @return the course with the matching name.
     * @throws CourseNotFoundException if a course with a matching name doesn't exists
     */
    private Course loadCourse(String courseName) throws CourseNotFoundException {
        return courseRepository.findById(courseName)
                .orElseThrow(() -> new CourseNotFoundException("Course " + courseName + " not found"));
    }

    /**
     * Loads the {@link Course} with a matching name only if the current logged in {@link Professor}
     * is one of the owners of the {@link Course}.
     *
     * @param courseName the name of the course to load to.
     * @return the course with the matching name.
     * @throws ProfessorNotAuthorizedException if the current professor is not authorized
     * @throws CourseNotFoundException         if a course with a matching name hasn't been found
     * @throws ProfessorNotFoundException      if the logged user is not a professor
     */
    private @NotNull Course loadCourseIfProfessorIsAuthorized(String courseName)
            throws ProfessorNotFoundException, CourseNotFoundException, ProfessorNotAuthorizedException {
        final Professor professor = loadCurrentProfessor();
        final Course course = loadCourse(courseName);
        // If course professor list doesn't contains the current professor,
        // the current professor is not authorized to access the course.
        if (!course.getProfessors().contains(professor)) {
            throw new ProfessorNotAuthorizedException("Professor " + professor.getId()
                    + " is not authorized to access course " + courseName);
        }
        return course;
    }

}
