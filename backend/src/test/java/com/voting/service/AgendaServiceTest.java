// src/test/java/com/voting/service/AgendaServiceTest.java
package com.voting.service;

import com.voting.dto.AgendaDTO;
import com.voting.exception.ResourceNotFoundException;
import com.voting.model.Agenda;
import com.voting.repository.AgendaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendaServiceTest {

    @Mock
    private AgendaRepository agendaRepository;

    @InjectMocks
    private AgendaService agendaService;

    @Test
    void createAgenda_Success() {
        // Arrange
        AgendaDTO inputDto = new AgendaDTO();
        inputDto.setTitle("Test Agenda");
        inputDto.setDescription("Test Description");

        Agenda savedAgenda = new Agenda();
        savedAgenda.setId(1L);
        savedAgenda.setTitle("Test Agenda");
        savedAgenda.setDescription("Test Description");

        when(agendaRepository.save(any(Agenda.class))).thenReturn(savedAgenda);

        // Act
        AgendaDTO result = agendaService.createAgenda(inputDto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Agenda", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        verify(agendaRepository, times(1)).save(any(Agenda.class));
    }

    @Test
    void getAllAgendas_Success() {
        // Arrange
        Agenda agenda1 = new Agenda();
        agenda1.setId(1L);
        agenda1.setTitle("Agenda 1");

        Agenda agenda2 = new Agenda();
        agenda2.setId(2L);
        agenda2.setTitle("Agenda 2");

        List<Agenda> agendas = Arrays.asList(agenda1, agenda2);

        when(agendaRepository.findAll()).thenReturn(agendas);

        // Act
        List<AgendaDTO> result = agendaService.getAllAgendas();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Agenda 1", result.get(0).getTitle());
        assertEquals("Agenda 2", result.get(1).getTitle());
    }

    @Test
    void getAgendaById_Success() {
        // Arrange
        Agenda agenda = new Agenda();
        agenda.setId(1L);
        agenda.setTitle("Test Agenda");

        when(agendaRepository.findById(1L)).thenReturn(Optional.of(agenda));

        // Act
        AgendaDTO result = agendaService.getAgendaById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Agenda", result.getTitle());
    }

    @Test
    void getAgendaById_NotFound() {
        // Arrange
        when(agendaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            agendaService.getAgendaById(1L);
        });
    }
}
