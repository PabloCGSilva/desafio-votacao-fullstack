// src/main/java/com/voting/model/VotingSession.java
package com.voting.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VotingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "agenda_id", nullable = false)
    private Agenda agenda;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private SessionStatus status;

    public enum SessionStatus {
        OPEN, CLOSED
    }

    public boolean isOpen() {
        LocalDateTime now = LocalDateTime.now();
        return status == SessionStatus.OPEN
                && now.isAfter(startTime)
                && now.isBefore(endTime);
    }
}
