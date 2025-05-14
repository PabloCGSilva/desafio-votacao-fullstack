// src/main/java/com/voting/service/VotingSessionService.java
package com.voting.service;

import com.voting.dto.OpenSessionDTO;
import com.voting.dto.VoteResultDTO;
import com.voting.exception.ResourceNotFoundException;
import com.voting.exception.VotingException;
import com.voting.model.Agenda;
import com.voting.model.VotingSession;
import com.voting.model.VotingSession.SessionStatus;
import com.voting.repository.VoteRepository;
import com.voting.repository.VotingSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VotingSessionService {

    private final VotingSessionRepository votingSessionRepository;
    private final VoteRepository voteRepository;
    private final AgendaService agendaService;

    /**
     * Opens a voting session for an agenda
     *
     * @param openSessionDTO DTO containing session opening information
     * @return Created voting session ID
     */
    @Transactional
    public Long openVotingSession(OpenSessionDTO openSessionDTO) {
        log.info("Opening voting session for agenda ID: {}", openSessionDTO.getAgendaId());

        // Check if there's already an open session for this agenda
        Optional<VotingSession> existingOpenSession
                = votingSessionRepository.findOpenSessionByAgendaId(openSessionDTO.getAgendaId());
        if (existingOpenSession.isPresent()) {
            throw new VotingException("There is already an open voting session for this agenda");
        }

        Agenda agenda = agendaService.getAgendaEntityById(openSessionDTO.getAgendaId());

        // Default duration is 1 minute if not specified
        int durationInMinutes = openSessionDTO.getDurationInMinutes() != null
                ? openSessionDTO.getDurationInMinutes() : 1;

        LocalDateTime now = LocalDateTime.now();
        VotingSession votingSession = new VotingSession();
        votingSession.setAgenda(agenda);
        votingSession.setStartTime(now);
        votingSession.setEndTime(now.plusMinutes(durationInMinutes));
        votingSession.setStatus(VotingSession.SessionStatus.OPEN);

        VotingSession savedSession = votingSessionRepository.save(votingSession);
        log.info("Voting session opened successfully with ID: {}", savedSession.getId());

        return savedSession.getId();
    }

    public List<VotingSession> findActiveSessions() {
        return votingSessionRepository.findByStatus(com.voting.model.VotingSession.SessionStatus.OPEN);
    }

    /**
     * Gets a voting session by ID
     *
     * @param id ID of the voting session
     * @return Voting session with the given ID
     * @throws ResourceNotFoundException if session not found
     */
    @Transactional(readOnly = true)
    public VotingSession getVotingSessionById(Long id) {
        log.info("Getting voting session with ID: {}", id);
        return votingSessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Voting session not found with ID: " + id));
    }

    /**
     * Gets the result of a voting session
     *
     * @param sessionId ID of the voting session
     * @return Voting result
     */
    @Transactional(readOnly = true)
    public VoteResultDTO getVotingResult(Long sessionId) {
        log.info("Getting voting result for session ID: {}", sessionId);

        VotingSession session = getVotingSessionById(sessionId);

        // Check if session is still open
        if (session.isOpen()) {
            throw new VotingException("Cannot get results while the voting session is still open");
        }

        int yesVotes = voteRepository.countYesVotesBySessionId(sessionId);
        int noVotes = voteRepository.countNoVotesBySessionId(sessionId);
        int totalVotes = yesVotes + noVotes;

        String result = "Tie";
        if (yesVotes > noVotes) {
            result = "Approved";
        } else if (noVotes > yesVotes) {
            result = "Rejected";
        }

        return new VoteResultDTO(
                session.getAgenda().getId(),
                session.getAgenda().getTitle(),
                sessionId,
                yesVotes,
                noVotes,
                totalVotes,
                result
        );
    }

    /**
     * Scheduled task to close expired voting sessions
     */
    @Scheduled(fixedRate = 60000) // Check every minute
    public void checkExpiredSessions() {
        List<VotingSession> openSessions = votingSessionRepository.findByStatus(com.voting.model.VotingSession.SessionStatus.OPEN);

        LocalDateTime now = LocalDateTime.now();
        for (VotingSession session : openSessions) {
            if (now.isAfter(session.getEndTime())) {
                session.setStatus(com.voting.model.VotingSession.SessionStatus.CLOSED);
                votingSessionRepository.save(session);
                log.info("Closed expired voting session: {}", session.getId());
            }
        }
    }
}
