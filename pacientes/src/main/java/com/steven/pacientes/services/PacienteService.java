package com.steven.pacientes.services;

import com.steven.pacientes.dto.paciente.PacienteRequest;
import com.steven.pacientes.dto.paciente.PacienteRespose;

public interface PacienteService extends CommonCrud<PacienteRequest, PacienteRespose>
{
    PacienteRespose obtenerPorIdSinValidarEstado(Long id);
}
