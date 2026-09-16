package com.steven.commons.clients;


import com.steven.commons.dto.pacientes.PacienteRespose;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("Pacientes")
public interface PacienteClient {

    @GetMapping("{idPaciente}")
PacienteRespose obtenerPacienteActivo(
        @PathVariable Long idPaciente
    );

}
