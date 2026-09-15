package com.steven.msv.citas.service;

import com.steven.commons.clients.MedicoClient;
import com.steven.commons.dto.medicos.MedicoResponse;
import com.steven.commons.dto.pacientes.PacienteRespose;
import com.steven.commons.enums.DisponibilidadMedico;
import com.steven.commons.enums.EstadoRegistro;
import com.steven.commons.exceptions.RecursoNoEncontradoException;
import com.steven.msv.citas.dto.CitaRequest;
import com.steven.msv.citas.dto.CitaResponse;
import com.steven.msv.citas.entity.Cita;
import com.steven.msv.citas.enums.EstadoCita;
import com.steven.msv.citas.mapper.CitaMapper;
import com.steven.msv.citas.repository.CitaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class CitaServiceImpl implements CitaService{

    private final CitaRepository citaRepository;

    private final CitaMapper citaMapper;

    private final MedicoClient medicoClient;

    @Override
    public void actualizarEstadoCita(Long idCita, Long idEstadoCita) {

        Cita cita = obtenerCitaOException(idCita);

        log.info("Actualizando cita con id {}",idCita);

        cita.actualizarEstadoCita(EstadoCita.obtenerEstadoCitaPorCodigo(idEstadoCita));

        log.info("Estado de la cita {} actualizado correctamente",idCita);

    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponse> listar() {
        return citaRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream().map(cita -> citaMapper.entidadAResponse(cita,
                obtenerPacienteSinEstado(cita.getIdPaciente()),
                obtenerMedicoSinEstado(cita.getIdMedico()))).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public CitaResponse obtenerPorId(Long id) {
//return citaMapper.entidadAResponse(obtenerCitaOException(id));

        Cita cita = obtenerCitaOException(id);

        return citaMapper.entidadAResponse(cita,null,obtenerMedicoSinEstado(cita.getIdMedico())
);
    }

    @Override
    public CitaResponse registrar(CitaRequest request) {
        log.info("Registrando nueva cita...");

        MedicoResponse medico = obtenerMedicoActivo(request.idMedico());

        validarMedicoActivoDisponible(medico);

        Cita cita= citaMapper.requestAEntidad(request);

        citaRepository.save(cita);

        medicoClient.actualizarDisponibilidadMedico(medico.id(),DisponibilidadMedico.NO_DISPONIBLE.getCodigo());

        log.info("Cita registrada exitosamente");

        return citaMapper.entidadAResponse(cita,null,null);
    }

    @Override
    public CitaResponse actualizar(CitaRequest request, long id) {
        Cita cita = obtenerCitaOException(id);

        MedicoResponse medico = obtenerMedicoActivo(request.idMedico());

        log.info("Actualizando cita con id {}",id);

        return citaMapper.entidadAResponse(
                cita,null,medico
        );
    }

    @Override
    public void eliminar(Long id) {

        Cita cita = obtenerCitaOException(id);

        log.info("Eliminando cita con id {}",id);

        cita.eliminar();

        log.info("Cita con id {} ja sido marcada como eliminada ");

    }

    private MedicoResponse obtenerMedicoActivo (Long id)
    {

        log.info("Buscando medico activo con id {} en el servicio remoto",id);

        return medicoClient.obtenerMedicoActivoPorId(id);

    }

    private MedicoResponse obtenerMedicoSinEstado (Long id)
    {

        log.info("Buscando medico sin estado con id {} en el servicio remoto",id);

        return medicoClient.obtenerMedicoSinEstadoPorId(id);

    }

    private Cita obtenerCitaOException (Long id)
    {
        log.info("Buscando cita con id : {} ",id);

        return citaRepository.findById(id).orElseThrow(
                ()-> new RecursoNoEncontradoException("Cita no encontrada con id :"+id)
        );
    }


    private PacienteRespose obtenerPacienteSinEstado (Long id)
    {

        log.info("Buscando paciente sin estado con id {} en el servicio remoto",id);

        return null;

    }

    private  void validarMedicoActivoDisponible(MedicoResponse medico)
    {

        log.info("Validando si el medico activo esta disponible");

        if (!DisponibilidadMedico.DISPONIBLE.getCodigo().equals(medico.idDisponibilidad()))
            throw  new IllegalStateException("El medico no esta disponible para consulta");

    }

    private void actualizarDisponibilidadMedico(Long iMedico,Long idDisponibilidad)
    {
        log.info("Actualizando disponibilidad del medico en el servicio remoto...");

        medicoClient.actualizarDisponibilidadMedico(iMedico, idDisponibilidad);

        log.info("Disponibilidad del medico actualizada en el servicio remoto");
    }


}
