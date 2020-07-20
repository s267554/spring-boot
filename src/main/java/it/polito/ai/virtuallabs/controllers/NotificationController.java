package it.polito.ai.virtuallabs.controllers;

import it.polito.ai.virtuallabs.services.NotificationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;

@Controller
@RequestMapping("")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/confirmAccount")
    public String confirmAccount(@RequestParam(name = "token") @NotBlank String token) {
        notificationService.confirmAccount(token);
        return "account_confirm_page";
    }

    @GetMapping("/confirmTeam")
    public String confirmTeam(@RequestParam(name = "token") @NotBlank String token) {
        notificationService.confirmTeam(token);
        return "team_confirm_page";
    }

    @GetMapping("/rejectTeam")
    public String rejectTeam(@RequestParam(name = "token") @NotBlank String token) {
        notificationService.rejectTeam(token);
        return "team_reject_page";
    }

}
