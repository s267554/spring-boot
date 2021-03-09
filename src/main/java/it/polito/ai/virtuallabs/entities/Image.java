package it.polito.ai.virtuallabs.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Image {

    @Id
    String id;

    @Lob
    byte[] content;

}
