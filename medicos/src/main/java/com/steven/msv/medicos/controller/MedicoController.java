package com.steven.msv.medicos.controller;

import com.steven.commons.controller.CrudController;
import com.steven.commons.dto.medicos.MedicoRequest;
import com.steven.commons.dto.medicos.MedicoResponse;
import com.steven.msv.medicos.service.MedicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "API Medicos ",description = "Metodos para la gestion de medicos ")
public class MedicoController extends CrudController<MedicoRequest, MedicoResponse, MedicoService> {


    public MedicoController (MedicoService service)
    {
        super(service);
    }

    @GetMapping("/id-medico/{id}")
    @Operation(summary = "Obtener medico por id sin importar el esto de registro")
    public ResponseEntity<MedicoResponse> obtenerMedicoPorIdSinEstado(
            @PathVariable @Positive(message = "El id debe de ser positivo" )Long id
    )
    {
        return ResponseEntity.ok(service.obtenerMedicoPorIdSinEstado(id));
    }

    @PutMapping("/{idMedico}/disponibilidad/{idDisponibilidad}")
    @Operation(summary = "Actualizar disponibilidad del medico "+
    " (No es endpoint libre,debe  gestionarlo el sistema de citas)")
    public ResponseEntity<Void> actualizarDisponibilidadMedico(
            @PathVariable @Positive(message = "El idMedico debe de ser positivo") Long idMedico,
            @PathVariable @Positive(message = "El idDisponibilidad debe de ser positivo") Long idDisponibilidad
    )
    {

        service.actualizarDisponibilidadDelMedico(idMedico,idDisponibilidad);
        return ResponseEntity.noContent().build();

    }



}
