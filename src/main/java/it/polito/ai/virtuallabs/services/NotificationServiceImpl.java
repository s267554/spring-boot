package it.polito.ai.virtuallabs.services;

import it.polito.ai.virtuallabs.controllers.NotificationController;
import it.polito.ai.virtuallabs.dtos.TeamDTO;
import it.polito.ai.virtuallabs.dtos.UserDTO;
import it.polito.ai.virtuallabs.entities.*;
import it.polito.ai.virtuallabs.repositories.AccountTokenRepository;
import it.polito.ai.virtuallabs.repositories.TeamRepository;
import it.polito.ai.virtuallabs.repositories.TeamTokenRepository;
import it.polito.ai.virtuallabs.repositories.UserRepository;
import it.polito.ai.virtuallabs.services.exceptions.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final AccountTokenRepository accountTokenRepository;

    private final TeamTokenRepository teamTokenRepository;

    private final UserRepository userRepository;

    private final TeamRepository teamRepository;

    private final JavaMailSender javaMailSender;

    public NotificationServiceImpl(AccountTokenRepository accountTokenRepository,
                                   TeamTokenRepository teamTokenRepository,
                                   UserRepository userRepository,
                                   TeamRepository teamRepository,
                                   JavaMailSender javaMailSender) {
        this.accountTokenRepository = accountTokenRepository;
        this.teamTokenRepository = teamTokenRepository;
        this.teamRepository = teamRepository;
        this.javaMailSender = javaMailSender;
        this.userRepository = userRepository;
    }


    @Override
    public void sendMessage(String address, String subject, String body) {

        final SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(address);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);

    }

    @Override
    public void confirmAccount(String token) {
        final AccountToken t = accountTokenRepository.findById(token)
                .orElseThrow(TokenNotFoundException::new);
        if (t.getExpiryDate().compareTo(new Date()) < 0) {
            accountTokenRepository.deleteById(token);
            throw new TokenExpiredException();
        }

        final User user = t.getUser();
        user.setEnabled(true);

        accountTokenRepository.deleteById(token);

        userRepository.save(user);

    }

    @Async
    @Override
    public void notifyAccount(@NotNull UserDTO userDTO) {
        final User user = userRepository.findByUsername(userDTO.getId())
                .orElseThrow(() -> new UsernameNotFoundException(""));

        final LocalDateTime expiryDate = LocalDateTime.now()
                .plus(1, ChronoUnit.DAYS);
        final Timestamp timestamp = Timestamp.valueOf(expiryDate);

        final AccountToken token = new AccountToken();
        token.setId(UUID.randomUUID().toString());
        token.setExpiryDate(timestamp);
        token.setUser(user);

        accountTokenRepository.save(token);

        final Link confirmLink = WebMvcLinkBuilder
                .linkTo(NotificationController.class)
                .slash("confirmAccount?token=" + token.getId())
                .withSelfRel();

        sendMessage(userDTO.getEmail(), "Confirm your account", confirmLink.toString());

    }

    @Override
    public boolean confirmTeam(String token) {
        final TeamToken t = teamTokenRepository.findById(token)
                .orElseThrow(TokenNotFoundException::new);

        final Team team = t.getTeam();
        final Team.Key key = t.getTeam().getKey();


        if (t.getExpiryDate().compareTo(new Date()) < 0) {
            // can't delete tokens
            // teamTokenRepository.deleteById(token);
            team.setInvalid(true);
            teamRepository.save(team);
            throw new TokenExpiredException();
        }


        if (team.isInvalid())
            // TODO: create new exception
            throw new TeamNotEnabledException("Team is not valid anymore");

        // can't be in any other active porposal
        @NotBlank String id = t.getStudent().getId();
        if (t.getTeam().getCourse().getTeams().stream()
                .filter(value -> value.getConfirmedIds().contains(id))
                .anyMatch(value -> (value.getExpiryDate().compareTo(new Date()) > 0 && !value.isInvalid())
                        || value.isEnabled())
        )
            throw new StudentAlreadyHasATeamException("Can't join more than one team");

        // can't delete tokens
        // teamTokenRepository.deleteById(token);

        // set confirmed members NOT based on existing tokens
        final List<String> newConfirmed = team.getConfirmedIds();
        newConfirmed.add(t.getStudent().getId());
        team.setConfirmedIds(newConfirmed);

        if (newConfirmed.size() != team.getMembers().size()) {
            teamRepository.save(team);
            return false;
        }

        team.setEnabled(true);
        teamRepository.save(team);

        return true;
    }

    @Override
    public void rejectTeam(String token) {
        final TeamToken t = teamTokenRepository.findById(token)
                .orElseThrow(TokenNotFoundException::new);
        final Team team = t.getTeam();
        final Team.Key key = team.getKey();

        // tokens can be deleted by the user via deleteTeam
        // teamTokenRepository.deleteAllByTeamId(key);
        // teams can NOT be deleted
        List<String> rejected = team.getRejectedIds();
        rejected.add(t.getStudent().getId());
        team.setRejectedIds(rejected);

        team.setInvalid(true);
        teamRepository.save(team);
    }

    @Async
    @Override
    public void notifyTeam(@NotNull TeamDTO teamDTO, @NotNull List<String> ids, Long timeout) {
        final Team team = teamRepository.findById(new Team.Key(teamDTO.getCourseName(), teamDTO.getName()))
                .orElseThrow(TeamNotFoundException::new);

        // add creator for token deleting purposes
        ids.add(teamDTO.getCreator());

        final List<User> users = ids.stream().map((id) ->
                userRepository.findByUsername(id)
                        .orElseThrow(() -> new UsernameNotFoundException(""))
        ).collect(Collectors.toList());

        final LocalDateTime expiryDate = LocalDateTime.now()
                .plus(timeout, ChronoUnit.SECONDS);

        final Timestamp timestamp = Timestamp.valueOf(expiryDate);

        final List<TeamToken> tokens = new ArrayList<>();
        for (User u : users) {
            final String uuid = UUID.randomUUID().toString();

            final TeamToken token = new TeamToken();
            token.setId(uuid);
            token.setExpiryDate(timestamp);
            token.setTeam(team);
            token.setStudent((Student) u);

            tokens.add(token);
        }

        teamTokenRepository.saveAll(tokens);

        ids.remove(teamDTO.getCreator());

        for (int i = 0; i < ids.size(); i++) {

            final String email = users.get(i).getEmail();

            final TeamToken token = tokens.get(i);

            final Link confirmLink = WebMvcLinkBuilder
                    .linkTo(NotificationController.class)
                    .slash("confirmTeam?token=" + token.getId())
                    .withSelfRel();

            final Link rejectLink = WebMvcLinkBuilder
                    .linkTo(NotificationController.class)
                    .slash("rejectTeam?token=" + token.getId())
                    .withSelfRel();

            sendMessage(email, "Team invitation " + teamDTO.getName(),
                    confirmLink.toString() + "\n" + rejectLink.toString());

        }
    }

    @Override
    public void deleteTeam(String token) {

        final TeamToken t = teamTokenRepository.findById(token)
                .orElseThrow(TokenNotFoundException::new);
        final Team team = t.getTeam();

        teamTokenRepository.deleteById(token);

        List<String> rejected = team.getRejectedIds();
        rejected.add(t.getStudent().getId());
        team.setRejectedIds(rejected);

        team.setInvalid(true);
        teamRepository.save(team);
    }

}
