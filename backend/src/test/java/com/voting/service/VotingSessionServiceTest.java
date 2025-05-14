// src/test/java/com/voting/service/VotingSessionServiceTest.java
package com.voting.service;

import com.voting.dto.OpenSessionDTO;
import com.voting.dto.VoteResultDTO;
import com.voting.exception.ResourceNotFoundException;
import com.voting.exception.VotingException;
import com.voting.model.Agenda;
import com.voting.model.VotingSession;
import com.voting.repository.VoteRepository;
import com.voting.repository.VotingSessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VotingSessionServiceTest {

    @Mock
    private VotingSessionRepository votingSessionRepository;

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private AgendaService agendaService;

    @InjectMocks
    private VotingSessionService votingSessionService;

    @Test
    void openVotingSession_Success() {
        // Arrange
        OpenSessionDTO openSessionDTO = new OpenSessionDTO();
        openSessionDTO.setAgendaId(1L);
        openSessionDTO.setDurationInMinutes(5);

        Agenda agenda = new Agenda();
        agenda.setId(1L);
        agenda.setTitle("Test Agenda");

        VotingSession savedSession = new VotingSession();
        savedSession.setId(1L);
        savedSession.setAgenda(agenda);
        savedSession.setStartTime(LocalDateTime.now());
        savedSession.setEndTime(LocalDateTime.now().plusMinutes(5));
        savedSession.setStatus(VotingSession.SessionStatus.OPEN);

        when(votingSessionRepository.findOpenSessionByAgendaId(1L)).thenReturn(Optional.empty());
        when(agendaService.getAgendaEntityById(1L)).thenReturn(agenda);
        when(votingSessionRepository.save(any(VotingSession.class))).thenReturn(savedSession);

        // Act
        Long result = votingSessionService.openVotingSession(openSessionDTO);

        // Assert
        assertEquals(1L, result);
        verify(votingSessionRepository, times(1)).save(any(VotingSession.class));
    }

    @Test
    void openVotingSession_AlreadyOpen() {
        // Arrange
        OpenSessionDTO openSessionDTO = new OpenSessionDTO();
        openSessionDTO.setAgendaId(1L);

        VotingSession existingSession = new VotingSession();
        existingSession.setId(1L);

        when(votingSessionRepository.findOpenSessionByAgendaId(1L)).thenReturn(Optional.of(existingSession));

        // Act & Assert
        assertThrows(VotingException.class, () -> {
            votingSessionService.openVotingSession(openSessionDTO);
        });
    }

    @Test
    void getVotingSessionById_Success() {
        // Arrange
        VotingSession session = new VotingSession();
        session.setId(1L);

        when(votingSessionRepository.findById(1L)).thenReturn(Optional.of(session));

        // Act
        VotingSession result = votingSessionService.getVotingSessionById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getVotingSessionById_NotFound() {
        // Arrange
        when(votingSessionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            votingSessionService.getVotingSessionById(1L);
        });
    }

    @Test
    void getVotingResult_Success() {
        // Arrange
        Agenda agenda = new Agenda();
        agenda.setId(1L);
        agenda.setTitle("Test Agenda");

        VotingSession session = new VotingSession();
        session.setId(1L);
        session.setAgenda(agenda);
        session.setStatus(VotingSession.SessionStatus.CLOSED);
        session.setStartTime(LocalDateTime.now().minusMinutes(10));
        session.setEndTime(LocalDateTime.now().minusMinutes(5));

        when(votingSessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(voteRepository.countYesVotesBySessionId(1L)).thenReturn(3);
        when(voteRepository.countNoVotesBySessionId(1L)).thenReturn(2);

        // Act
        VoteResultDTO result = votingSessionService.getVotingResult(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getAgendaId());
        assertEquals("Test Agenda", result.getAgendaTitle());
        assertEquals(1L, result.getSessionId());
        assertEquals(3, result.getYesVotes());
        assertEquals(2, result.getNoVotes());
        assertEquals(5, result.getTotalVotes());
        assertEquals("Approved", result.getResult());
    }
}
