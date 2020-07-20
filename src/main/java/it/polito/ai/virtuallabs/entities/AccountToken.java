package it.polito.ai.virtuallabs.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
@Table(name = "account_token")
@EqualsAndHashCode(callSuper = true)
public class AccountToken extends Token {

    @OneToOne
    @JoinColumn(name = "user_id")
    User user;

}
