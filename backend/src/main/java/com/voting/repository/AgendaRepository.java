// src/main/java/com/voting/repository/AgendaRepository.java
package com.voting.repository;

import com.voting.model.Agenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendaRepository extends JpaRepository<Agenda, Long> {
}
