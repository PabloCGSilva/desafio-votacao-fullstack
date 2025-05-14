// src/main/java/com/voting/dto/OpenSessionDTO.java
package com.voting.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OpenSessionDTO {

    @NotNull(message = "Agenda ID is required")
    private Long agendaId;

    private Integer durationInMinutes;
}
