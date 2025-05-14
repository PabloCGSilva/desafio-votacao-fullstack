// src/main/java/com/voting/repository/VotingSessionRepository.java
package com.voting.repository;

import com.voting.model.Agenda;
import com.voting.model.VotingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VotingSessionRepository extends JpaRepository<VotingSession, Long> {

    List<VotingSession> findByAgenda(Agenda agenda);

    List<VotingSession> findByStatus(VotingSession.SessionStatus status);

    @Query("SELECT vs FROM VotingSession vs WHERE vs.agenda.id = :agendaId AND vs.status = 'OPEN'")
    Optional<VotingSession> findOpenSessionByAgendaId(@Param("agendaId") Long agendaId);
}
