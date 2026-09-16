package com.steven.msv.citas.controller;

import com.steven.commons.controller.CrudController;
import com.steven.msv.citas.dto.CitaRequest;
import com.steven.msv.citas.dto.CitaResponse;
import com.steven.msv.citas.service.CitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Validated
@Tag(name="API Citas",description = "Metodos para la gestion de citas")
public class CitaController extends CrudController<CitaRequest, CitaResponse, CitaService> {

    public CitaController(CitaService service) {
        super(service);
    }

    @Operation(
            summary = "Actualizar el estado de la cita",
            description = "Actualiza el estado de una cita utilizando el identificador de la cita y el identificador del nuevo estado"
    )
    @PatchMapping("/{idCita}/estado/{idEstado}")
    public ResponseEntity<Void> actualizarEstadoCita(
            @PathVariable @Positive(message = "El idCita debe ser positivo") Long idCita,
            @PathVariable @Positive(message = "El idEstado debe ser positivo") Long idEstado)
    {
        service.actualizarEstadoCita(idCita,idEstado);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Verificar si un paciente tiene citas confirmadas o en curso",
            description = "El metodo recibe el id del paciente, y verifica si en la tabla de citas tiene citas confirmadas o en curso "
    )

    @GetMapping("/pacientes/{idPaciente}/validar-existen-confirmadas-en-curso")
    public ResponseEntity <Void> validarExistenPacientesConfirmadasOCurso
            (
               @PathVariable @Positive(message = "El id del paciente debe de ser positivo") Long idPaciente
            )
    {
      service.validarCitasBloqueantesPaciente(idPaciente);

      return ResponseEntity.noContent().build();
    }


}
