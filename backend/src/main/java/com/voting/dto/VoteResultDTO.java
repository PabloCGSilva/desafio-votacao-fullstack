// src/main/java/com/voting/dto/VoteResultDTO.java
package com.voting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteResultDTO {

    private Long agendaId;
    private String agendaTitle;
    private Long sessionId;
    private int yesVotes;
    private int noVotes;
    private int totalVotes;
    private String result;
}
