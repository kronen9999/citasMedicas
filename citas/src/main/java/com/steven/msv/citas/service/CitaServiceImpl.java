package com.steven.msv.citas.service;

import com.steven.commons.clients.MedicoClient;
import com.steven.commons.dto.medicos.MedicoResponse;
import com.steven.commons.dto.pacientes.PacienteRespose;
import com.steven.commons.enums.DisponibilidadMedico;
import com.steven.commons.enums.EstadoRegistro;
import com.steven.commons.exceptions.RecursoNoEncontradoException;
import com.steven.commons.clients.PacienteClient;
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

import java.util.EnumSet;
import java.util.List;


@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class CitaServiceImpl implements CitaService{

    private final CitaRepository citaRepository;

    private final CitaMapper citaMapper;

    private final MedicoClient medicoClient;

    private final PacienteClient pacienteClient;

    @Override
    public void actualizarEstadoCita(Long idCita, Long idEstadoCita) {

        Cita cita = obtenerCitaOException(idCita);

        log.info("Actualizando cita con id {}",idCita);

        cita.actualizarEstadoCita(EstadoCita.obtenerEstadoCitaPorCodigo(idEstadoCita));

        citaRepository.save(cita);

        cambiarDisponibilidadMedicoSegunEstadoCita(cita.getIdMedico(),cita.getEstadoCita());

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

        return citaMapper.entidadAResponse(cita,obtenerPacienteSinEstado(cita.getIdPaciente()),obtenerMedicoSinEstado(cita.getIdMedico())
);
    }

    @Override
    public CitaResponse registrar(CitaRequest request) {
        log.info("Registrando nueva cita...");

        MedicoResponse medico = obtenerMedicoActivo(request.idMedico());

        PacienteRespose paciente =pacienteClient.obtenerPacienteActivo(request.idPaciente());

        validarPacienteCitaActivaSimultanea(request.idPaciente());

        validarMedicoActivoDisponible(medico);

        Cita cita= citaMapper.requestAEntidad(request);

        citaRepository.save(cita);

       cambiarDisponibilidadMedicoSegunEstadoCita(request.idMedico(),cita.getEstadoCita());

        log.info("Cita registrada exitosamente");

        return citaMapper.entidadAResponse(cita,paciente,medico);
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

        if (cita.getEstadoCita()==EstadoCita.PENDIENTE)
            actualizarDisponibilidadMedico(cita.getIdMedico(),DisponibilidadMedico.DISPONIBLE.getCodigo());

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

        return pacienteClient.obtenerPacienteSinValidarEstado(id);

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

    private void cambiarDisponibilidadMedicoSegunEstadoCita(Long idMedico,EstadoCita estadoCita)
    {

        switch (estadoCita)
        {
            case PENDIENTE,CONFIRMADA -> actualizarDisponibilidadMedico(idMedico,DisponibilidadMedico.NO_DISPONIBLE.getCodigo());
            case EN_CURSO ->actualizarDisponibilidadMedico(idMedico,DisponibilidadMedico.EN_CONSULTA.getCodigo());
            case FINALIZADA,CANCELADA ->actualizarDisponibilidadMedico(idMedico,DisponibilidadMedico.DISPONIBLE.getCodigo());

        }

    }

    private void validarPacienteCitaActivaSimultanea(Long idPaciente)
    {

        if (citaRepository.existsByIdPacienteAndEstadoRegistroAndEstadoCitaIn(idPaciente
                ,EstadoRegistro.ACTIVO,
                EnumSet.of(EstadoCita.PENDIENTE,EstadoCita.CONFIRMADA,EstadoCita.EN_CURSO)))
            throw  new IllegalStateException("EL paciente no se pude registrar por que cuenta con una cita es estado pendiente , confirmada o en curso");

    }




}
