    // src/main/java/com/voting/service/VoteService.java
package com.voting.service;

import com.voting.client.UserInfoClient;
import com.voting.dto.VoteDTO;
import com.voting.exception.ResourceNotFoundException;
import com.voting.exception.VotingException;
import com.voting.model.Vote;
import com.voting.model.VotingSession;
import com.voting.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoteService {

    private final VoteRepository voteRepository;
    private final VotingSessionService votingSessionService;
    private final UserInfoClient userInfoClient;

    /**
     * Registers a vote in a voting session
     *
     * @param voteDTO DTO containing vote information
     * @return ID of the registered vote
     */
    public void validateVote(String cpf, String associateId, Long sessionId) {
        log.info("Validating vote eligibility for associate: {} with CPF: {} in session: {}",
                associateId, cpf, sessionId);

        // Validate if CPF is valid and user can vote
        try {
            UserInfoClient.VoteAbility ability = userInfoClient.checkUserAbilityToVote(cpf);
            if (ability == UserInfoClient.VoteAbility.UNABLE_TO_VOTE) {
                throw new VotingException("Associate is not able to vote");
            }
        } catch (UserInfoClient.NotFoundException e) {
            throw new ResourceNotFoundException("Invalid CPF: " + cpf);
        }

        // Get voting session
        VotingSession session = votingSessionService.getVotingSessionById(sessionId);

        // Check if session is open
        if (!session.isOpen()) {
            throw new VotingException("Voting session is not open");
        }

        // Check if associate has already voted in this session
        Optional<Vote> existingVote = voteRepository
                .findByAssociateIdAndVotingSessionId(associateId, sessionId);
        if (existingVote.isPresent()) {
            throw new VotingException("Associate has already voted in this session");
        }

        // If we get here, the validation passed
    }

    @Transactional
    public Long vote(VoteDTO voteDTO) {
        log.info("Registering vote for associate: {} in session: {}",
                voteDTO.getAssociateId(), voteDTO.getSessionId());

        // Validate if CPF is valid and user can vote
        try {
            UserInfoClient.VoteAbility ability = userInfoClient.checkUserAbilityToVote(voteDTO.getCpf());
            if (ability == UserInfoClient.VoteAbility.UNABLE_TO_VOTE) {
                throw new VotingException("Associate is not able to vote");
            }
        } catch (UserInfoClient.NotFoundException e) {
            throw new ResourceNotFoundException("Invalid CPF: " + voteDTO.getCpf());
        }

        // Get voting session
        VotingSession session = votingSessionService.getVotingSessionById(voteDTO.getSessionId());

        // Check if session is open
        if (!session.isOpen()) {
            throw new VotingException("Voting session is not open");
        }

        // Check if associate has already voted in this session
        Optional<Vote> existingVote = voteRepository
                .findByAssociateIdAndVotingSessionId(voteDTO.getAssociateId(), voteDTO.getSessionId());
        if (existingVote.isPresent()) {
            throw new VotingException("Associate has already voted in this session");
        }

        // Create and save the vote
        Vote vote = new Vote();
        vote.setAssociateId(voteDTO.getAssociateId());
        vote.setVotingSession(session);
        vote.setVoteOption(voteDTO.getVoteOption() ? Vote.VoteOption.YES : Vote.VoteOption.NO);

        Vote savedVote = voteRepository.save(vote);
        log.info("Vote registered successfully with ID: {}", savedVote.getId());

        return savedVote.getId();
    }
}
