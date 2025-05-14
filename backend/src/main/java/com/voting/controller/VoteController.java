// src/main/java/com/voting/controller/VoteController.java
package com.voting.controller;

import com.voting.service.VoteService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.voting.dto.VoteDTO;
import com.voting.client.UserInfoClient;
import com.voting.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v1/votes")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;
    private final UserInfoClient userInfoClient;
    private final VoteRepository voteRepository;

    // Your existing POST method for voting
    @GetMapping("/validate")
    public ResponseEntity<Void> validateVoteEligibility(
            @RequestParam String cpf,
            @RequestParam String associateId,
            @RequestParam Long sessionId) {

        // Call a service method instead of having logic in controller
        voteService.validateVote(cpf, associateId, sessionId);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Long> registerVote(@RequestBody VoteDTO voteDTO) {
        Long voteId = voteService.vote(voteDTO);
        return ResponseEntity.ok(voteId);
    }
}
