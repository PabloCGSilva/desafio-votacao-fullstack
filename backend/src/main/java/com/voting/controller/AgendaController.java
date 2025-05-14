package com.voting.controller;

import com.voting.dto.AgendaDTO;
import com.voting.service.AgendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/agendas")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Agenda", description = "Agenda management endpoints")
public class AgendaController {

    private final AgendaService agendaService;

    @PostMapping
    @Operation(summary = "Create a new agenda")
    public ResponseEntity<AgendaDTO> createAgenda(@Valid @RequestBody AgendaDTO agendaDTO) {
        log.info("REST request to create agenda: {}", agendaDTO);
        AgendaDTO createdAgenda = agendaService.createAgenda(agendaDTO);
        return new ResponseEntity<>(createdAgenda, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all agendas")
    public ResponseEntity<List<AgendaDTO>> getAllAgendas() {
        log.info("REST request to get all agendas");
        List<AgendaDTO> agendas = agendaService.getAllAgendas();
        return ResponseEntity.ok(agendas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get agenda by ID")
    public ResponseEntity<AgendaDTO> getAgendaById(@PathVariable Long id) {
        log.info("REST request to get agenda with ID: {}", id);
        AgendaDTO agenda = agendaService.getAgendaById(id);
        return ResponseEntity.ok(agenda);
    }
}
