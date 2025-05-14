// src/test/java/com/voting/controller/AgendaControllerTest.java
package com.voting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voting.dto.AgendaDTO;
import com.voting.service.AgendaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AgendaController.class)
class AgendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AgendaService agendaService;

    @Test
    void createAgenda_Success() throws Exception {
        // Arrange
        AgendaDTO inputDto = new AgendaDTO();
        inputDto.setTitle("Test Agenda");
        inputDto.setDescription("Test Description");

        AgendaDTO outputDto = new AgendaDTO();
        outputDto.setId(1L);
        outputDto.setTitle("Test Agenda");
        outputDto.setDescription("Test Description");

        when(agendaService.createAgenda(any(AgendaDTO.class))).thenReturn(outputDto);

        // Act & Assert
        mockMvc.perform(post("/api/v1/agendas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Agenda"))
                .andExpect(jsonPath("$.description").value("Test Description"));
    }

    @Test
    void getAllAgendas_Success() throws Exception {
        // Arrange
        AgendaDTO agenda1 = new AgendaDTO();
        agenda1.setId(1L);
        agenda1.setTitle("Agenda 1");

        AgendaDTO agenda2 = new AgendaDTO();
        agenda2.setId(2L);
        agenda2.setTitle("Agenda 2");

        List<AgendaDTO> agendas = Arrays.asList(agenda1, agenda2);

        when(agendaService.getAllAgendas()).thenReturn(agendas);

        // Act & Assert
        mockMvc.perform(get("/api/v1/agendas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Agenda 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Agenda 2"));
    }
}
