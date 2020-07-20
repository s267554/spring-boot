package it.polito.ai.virtuallabs.entities;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "tokens")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Token {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    String id;

    @Type(type = "timestamp")
    @Column(name = "expiry_date", nullable = false, updatable = false)
    Date expiryDate;

}
