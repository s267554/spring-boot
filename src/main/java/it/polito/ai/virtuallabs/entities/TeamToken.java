package it.polito.ai.virtuallabs.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
@Table(name = "team_token")
@EqualsAndHashCode(callSuper = true)
public class TeamToken extends Token {

    @OneToOne
    @JoinColumns({@JoinColumn(name = "course_name"), @JoinColumn(name = "team_name")})
    Team team;

}
