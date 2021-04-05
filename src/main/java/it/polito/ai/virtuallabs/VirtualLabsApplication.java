package it.polito.ai.virtuallabs;

import it.polito.ai.virtuallabs.config.SpringAsyncConfig;
import it.polito.ai.virtuallabs.dtos.CourseDTO;
import it.polito.ai.virtuallabs.dtos.PaperDTO;
import it.polito.ai.virtuallabs.dtos.TeamDTO;
import it.polito.ai.virtuallabs.dtos.TeamEmbeddedDTO;
import it.polito.ai.virtuallabs.entities.Course;
import it.polito.ai.virtuallabs.entities.Paper;
import it.polito.ai.virtuallabs.entities.Team;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Import(SpringAsyncConfig.class)
@SpringBootApplication
public class VirtualLabsApplication {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NotNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods(CorsConfiguration.ALL);
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {
        final ModelMapper modelMapper = new ModelMapper();

        // type map inheritance ??
        modelMapper.typeMap(TeamDTO.class, Team.class)
                .addMappings(
                        new PropertyMap<TeamDTO, Team>() {
                            @Override
                            protected void configure() {
                                using(ctx -> new Team.Key(
                                        ((TeamDTO) ctx.getSource()).getCourseName(),
                                        ((TeamDTO) ctx.getSource()).getName())
                                )
                                        .map(source, destination.getKey());
                            }
                        });
        //.include(TeamEmbeddedDTO.class, Team.class);
        modelMapper.typeMap(TeamEmbeddedDTO.class, Team.class)
                .addMappings(
                        new PropertyMap<TeamEmbeddedDTO, Team>() {
                            @Override
                            protected void configure() {
                                using(ctx -> new Team.Key(
                                        ((TeamEmbeddedDTO) ctx.getSource()).getCourseName(),
                                        ((TeamEmbeddedDTO) ctx.getSource()).getName())
                                )
                                        .map(source, destination.getKey());
                            }
                        });

        modelMapper.typeMap(Team.class, TeamDTO.class)
                .addMapping(src -> src.getKey().getCourseName(), TeamDTO::setCourseName)
                .addMapping(src -> src.getKey().getName(), TeamDTO::setName);
        //.include(Team.class, TeamEmbeddedDTO.class);
        modelMapper.typeMap(Team.class, TeamEmbeddedDTO.class)
                .addMapping(src -> src.getKey().getCourseName(), TeamEmbeddedDTO::setCourseName)
                .addMapping(src -> src.getKey().getName(), TeamEmbeddedDTO::setName);

        modelMapper.typeMap(Paper.class, PaperDTO.class)
                .addMapping(src -> src.getKey().getAssignmentId(), PaperDTO::setAssignmentId);

        modelMapper.typeMap(CourseDTO.class, Course.class).addMappings(mapper -> mapper.skip(Course::setProfessors));

        return modelMapper;
    }

    public static void main(String[] args) {
        SpringApplication.run(VirtualLabsApplication.class, args);
    }

}
