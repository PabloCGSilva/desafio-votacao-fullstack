// src/main/java/com/voting/dto/AgendaDTO.java
package com.voting.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AgendaDTO {

    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;
}
