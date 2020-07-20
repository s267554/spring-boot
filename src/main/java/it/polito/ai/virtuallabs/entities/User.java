package it.polito.ai.virtuallabs.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role", discriminatorType = DiscriminatorType.STRING)
public abstract class User {

    @Id
    @NotBlank
    @Column(name = "id", nullable = false, updatable = false)
    String id;

    @NotBlank
    @Column(name = "name", updatable = false, nullable = false)
    String name;

    @NotBlank
    @Column(name = "surname", updatable = false, nullable = false)
    String surname;

    @Column(name = "photo_url")
    String photoUrl;

    @Email
    @NotNull
    @Column(name = "email", updatable = false, nullable = false, unique = true)
    String email;

    @Column(name = "enabled")
    boolean enabled;

    @NotBlank
    @Column(name = "password", nullable = false)
    String password;

    @NotBlank
    @Column(name = "role", insertable = false, updatable = false, nullable = false)
    String role;

}
