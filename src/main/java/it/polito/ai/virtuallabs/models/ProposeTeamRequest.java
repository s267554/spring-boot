package it.polito.ai.virtuallabs.models;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ProposeTeamRequest {

    @NotBlank
    private String name;

    @NotNull
    @Min(3600)
    private Long timeout;

    @NotNull
    @NotEmpty
    private List<String> ids;

}
