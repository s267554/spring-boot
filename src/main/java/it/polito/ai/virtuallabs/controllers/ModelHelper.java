package it.polito.ai.virtuallabs.controllers;

import it.polito.ai.virtuallabs.dtos.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

public final class ModelHelper {

    private ModelHelper() {

    }

    @Contract("_ -> param1")
    public static @NotNull CourseDTO enrich(@NotNull CourseDTO courseDTO) {

        final String name = courseDTO.getName();

        final Link enrolledLink = WebMvcLinkBuilder
                .linkTo(
                        WebMvcLinkBuilder.methodOn(CourseController.class)
                                .enrolled(name)
                ).withSelfRel();

        final Link teamsLink = WebMvcLinkBuilder
                .linkTo(
                        WebMvcLinkBuilder.methodOn(TeamController.class)
                                .getTeams(name, "")
                ).withSelfRel();

        courseDTO.add(enrolledLink);
        courseDTO.add(teamsLink);

        return courseDTO;

    }

    @Contract("_ -> param1")
    public static @NotNull StudentDTO enrich(@NotNull StudentDTO studentDTO) {


        return studentDTO;
    }

    @Contract("_ -> param1")
    public static @NotNull TeamDTO enrich(@NotNull TeamDTO teamDTO) {

        final Link membersLink = WebMvcLinkBuilder
                .linkTo(
                        WebMvcLinkBuilder.methodOn(TeamController.class)
                                .getVMsOfTeam(teamDTO.getCourseName(), teamDTO.getName())
                ).withSelfRel();

        teamDTO.add(membersLink);

        return teamDTO;
    }

    public static VirtualMachineDTO enrich(VirtualMachineDTO virtualMachineDTO) {

        return virtualMachineDTO;
    }

    public static AssignmentDTO enrich(AssignmentDTO assignmentDTO) {
        return assignmentDTO;
    }

    public static PaperDTO enrich(PaperDTO paperDTO) {
        return paperDTO;
    }
}
