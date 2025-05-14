// src/main/java/com/voting/service/AgendaService.java
package com.voting.service;

import com.voting.dto.AgendaDTO;
import com.voting.exception.ResourceNotFoundException;
import com.voting.model.Agenda;
import com.voting.repository.AgendaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgendaService {

    private final AgendaRepository agendaRepository;

    /**
     * Creates a new agenda
     *
     * @param agendaDTO DTO containing agenda information
     * @return Created agenda
     */
    @Transactional
    public AgendaDTO createAgenda(AgendaDTO agendaDTO) {
        log.info("Creating new agenda: {}", agendaDTO);

        Agenda agenda = new Agenda();
        agenda.setTitle(agendaDTO.getTitle());
        agenda.setDescription(agendaDTO.getDescription());

        Agenda savedAgenda = agendaRepository.save(agenda);
        log.info("Agenda created successfully with ID: {}", savedAgenda.getId());

        AgendaDTO result = new AgendaDTO();
        result.setId(savedAgenda.getId());
        result.setTitle(savedAgenda.getTitle());
        result.setDescription(savedAgenda.getDescription());

        return result;
    }

    /**
     * Gets all agendas
     *
     * @return List of all agendas
     */
    @Transactional(readOnly = true)
    public List<AgendaDTO> getAllAgendas() {
        log.info("Getting all agendas");
        return agendaRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Gets an agenda by ID
     *
     * @param id ID of the agenda
     * @return Agenda with the given ID
     * @throws ResourceNotFoundException if agenda not found
     */
    @Transactional(readOnly = true)
    public AgendaDTO getAgendaById(Long id) {
        log.info("Getting agenda with ID: {}", id);
        return agendaRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Agenda not found with ID: " + id));
    }

    /**
     * Gets an agenda entity by ID
     *
     * @param id ID of the agenda
     * @return Agenda entity with the given ID
     * @throws ResourceNotFoundException if agenda not found
     */
    @Transactional(readOnly = true)
    public Agenda getAgendaEntityById(Long id) {
        log.info("Getting agenda entity with ID: {}", id);
        return agendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agenda not found with ID: " + id));
    }

    private AgendaDTO convertToDTO(Agenda agenda) {
        AgendaDTO dto = new AgendaDTO();
        dto.setId(agenda.getId());
        dto.setTitle(agenda.getTitle());
        dto.setDescription(agenda.getDescription());
        return dto;
    }
}
