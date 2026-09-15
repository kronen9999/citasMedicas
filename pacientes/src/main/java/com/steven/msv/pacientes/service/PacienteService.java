package com.steven.msv.pacientes.service;

import com.steven.commons.dto.pacientes.PacienteRequest;
import com.steven.commons.dto.pacientes.PacienteRespose;
import com.steven.commons.service.CrudService;

public interface PacienteService extends CrudService<PacienteRequest, PacienteRespose> {

    PacienteRespose obtenerPorIdSinValidarEstado(Long id);
}
