package it.polito.ai.virtuallabs.dtos;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
public class PaperVersionDTO extends RepresentationModel<PaperVersionDTO> {

    Long id;

    Date date;

    String contentUrl;

}
