package com.steven.msv.citas.service;

import com.steven.commons.service.CrudService;
import com.steven.msv.citas.dto.CitaRequest;
import com.steven.msv.citas.dto.CitaResponse;

public interface CitaService extends CrudService<CitaRequest, CitaResponse> {

    void actualizarEstadoCita ( Long idCita,Long idEstadoCita);

}
