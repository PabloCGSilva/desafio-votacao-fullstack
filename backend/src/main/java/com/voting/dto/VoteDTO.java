// src/main/java/com/voting/dto/VoteDTO.java
package com.voting.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VoteDTO {

    @NotNull(message = "Session ID is required")
    private Long sessionId;

    @NotBlank(message = "Associate ID is required")
    private String associateId;

    @NotNull(message = "Vote option is required")
    private Boolean voteOption; // true for YES, false for NO

    @NotBlank(message = "CPF is required")
    private String cpf;
}
