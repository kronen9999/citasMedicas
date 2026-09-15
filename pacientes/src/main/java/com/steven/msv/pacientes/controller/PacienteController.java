package com.steven.msv.pacientes.controller;

import com.steven.commons.controller.CrudController;
import com.steven.commons.dto.pacientes.PacienteRequest;
import com.steven.commons.dto.pacientes.PacienteRespose;
import com.steven.msv.pacientes.service.PacienteService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Validated
@RequestMapping("api/pacientes")
public class PacienteController extends CrudController<PacienteRequest, PacienteRespose, PacienteService> {

    public PacienteController(PacienteService service) {
        super(service);
    }

    @GetMapping("/id-paciente/{id}")
    public ResponseEntity<PacienteRespose> obtenerIdSinValidarEstado(
            @PathVariable @Positive(message = "El id debe de ser positivo") Long id
    )
    {
        return ResponseEntity.ok(service.obtenerPorIdSinValidarEstado(id));
    }
}
