package com.steven.msv.medicos.service;

import com.steven.commons.dto.medicos.MedicoRequest;
import com.steven.commons.dto.medicos.MedicoResponse;
import com.steven.commons.service.CrudService;

public interface MedicoService extends CrudService<MedicoRequest, MedicoResponse> {

    MedicoResponse obtenerMedicoPorIdSinEstado(Long id);

    void actualizarDisponibilidadDelMedico(Long idMedico,Long idDisponibilidad);

    void actualizarDisponibilidadInterna(Long idMedico,Long idDisponibilidad);
}
