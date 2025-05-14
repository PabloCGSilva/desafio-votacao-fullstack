// src/main/java/com/voting/controller/VotingSessionController.java
package com.voting.controller;

import com.voting.dto.OpenSessionDTO;
import com.voting.dto.VoteResultDTO;
import com.voting.service.VotingSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.voting.model.VotingSession;

@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Voting Session", description = "Voting session management endpoints")
public class VotingSessionController {

    private final VotingSessionService votingSessionService;

    @PostMapping
    @Operation(summary = "Open a new voting session")
    public ResponseEntity<Long> openVotingSession(@Valid @RequestBody OpenSessionDTO openSessionDTO) {
        log.info("REST request to open voting session: {}", openSessionDTO);
        Long sessionId = votingSessionService.openVotingSession(openSessionDTO);
        return new ResponseEntity<>(sessionId, HttpStatus.CREATED);
    }

    @GetMapping("/active")
    public List<Map<String, Object>> getActiveSessions() {
        List<VotingSession> activeSessions = votingSessionService.findActiveSessions();
        return activeSessions.stream()
                .map(session -> {
                    Map<String, Object> sessionMap = new HashMap<>();
                    sessionMap.put("id", session.getId());
                    sessionMap.put("agendaId", session.getAgenda().getId());
                    return sessionMap;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/results")
    @Operation(summary = "Get voting session results")
    public ResponseEntity<VoteResultDTO> getVotingResult(@PathVariable Long id) {
        log.info("REST request to get voting result for session ID: {}", id);
        VoteResultDTO result = votingSessionService.getVotingResult(id);
        return ResponseEntity.ok(result);
    }
}
