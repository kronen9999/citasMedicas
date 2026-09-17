package com.steven.commons.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("citas")
public interface CitaClient {

    @GetMapping("/pacientes/{idPaciente}/validar-existen-confirmadas-en-curso")
    void validarExistenPacientesConfirmadasOCurso (@PathVariable  Long idPaciente);

    @GetMapping("/medicos/{idMedico}/validar-existen-confirmadas-en-curso")
    void validarExistenMedicosConfirmadasOCurso (@PathVariable  Long idMedico);
}
