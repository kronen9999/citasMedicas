package com.steven.msv.citas.service;

import com.steven.commons.clients.MedicoClient;
import com.steven.commons.dto.medicos.MedicoResponse;
import com.steven.commons.dto.pacientes.PacienteRespose;
import com.steven.commons.enums.DisponibilidadMedico;
import com.steven.commons.enums.EstadoRegistro;
import com.steven.commons.exceptions.EntidadRelacionadaException;
import com.steven.commons.exceptions.RecursoNoEncontradoException;
import com.steven.commons.clients.PacienteClient;
import com.steven.commons.utils.StringCustomUtils;
import com.steven.commons.utils.ValoresNumericosUtils;
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
import java.util.stream.Collectors;


@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class CitaServiceImpl implements CitaService{

    private final CitaRepository citaRepository;

    private final CitaMapper citaMapper;

    private final MedicoClient medicoClient;

    private final PacienteClient pacienteClient;

    private static final EnumSet<EstadoCita> ESTADOS_CITA_ACTIVA =
            EnumSet.of(EstadoCita.PENDIENTE,EstadoCita.CONFIRMADA,EstadoCita.EN_CURSO);

    private static final EnumSet<EstadoCita> ESTADOS_CITA_BLOQUEANTE =
            EnumSet.of(EstadoCita.CONFIRMADA,EstadoCita.EN_CURSO);



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

        if (cita.getEstadoCita()!=EstadoCita.PENDIENTE && cita.getEstadoCita()!=EstadoCita.CONFIRMADA)

            throw  new IllegalStateException("La cita no se puede actualizar por que no se encuentra como pendiente o confirmada");

        Long idMedicoAnterior = cita.getIdMedico();

        Long idPacienteAnterior = cita.getIdPaciente();

        log.info("Actualizando cita con id {}",id);

        cita.actualizar(request.idPaciente(),request.idMedico(),request.fechaCita(),request.sintomas());

        MedicoResponse medico = obtenerMedicoActivo(request.idMedico());

        PacienteRespose paciente = pacienteClient.obtenerPacienteActivo(request.idPaciente());

        procesarCambiosDeAsignacion(request,cita,medico,idMedicoAnterior,idPacienteAnterior);

        log.info("Cita con id {} actualizada correctamente",id);

        return citaMapper.entidadAResponse(cita,paciente,medico);
    }

    @Override
    public void eliminar(Long id) {

        Cita cita = obtenerCitaOException(id);

        log.info("Eliminando cita con id {}",id);

        cita.eliminar();

        if (cita.getEstadoCita()==EstadoCita.PENDIENTE)
            actualizarDisponibilidadMedico(cita.getIdMedico(),DisponibilidadMedico.DISPONIBLE.getCodigo());

        log.info("Cita con id {} ja sido marcada como eliminada ",id);

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
                ESTADOS_CITA_ACTIVA))
            throw  new IllegalStateException("EL paciente no se pude registrar por que cuenta con una cita es estado pendiente , confirmada o en curso");

    }

    private void validarPacienteSinOtrasCitasActivas(Long idPaciente,Long idCita)
    {

        if (citaRepository.existsByIdPacienteAndEstadoRegistroAndEstadoCitaInAndIdNot(idPaciente
                ,EstadoRegistro.ACTIVO,
                ESTADOS_CITA_ACTIVA,
                idCita))
            throw  new IllegalStateException("El paciente nuevo ya cuenta con una cita en estado pendiente, confirmada o en curso");

    }

    private void procesarCambiosDeAsignacion(CitaRequest request,Cita cita,MedicoResponse medico,
                                             Long idMedicoAnterior,Long idPacienteAnterior)
    {

        boolean cambioMedico = !request.idMedico().equals(idMedicoAnterior);

        boolean cambioPaciente = !request.idPaciente().equals(idPacienteAnterior);

        if (cambioPaciente)
            validarPacienteSinOtrasCitasActivas(request.idPaciente(),cita.getId());

        if (cambioMedico)
        {
            validarMedicoActivoDisponible(medico);

            actualizarDisponibilidadMedico(idMedicoAnterior,DisponibilidadMedico.DISPONIBLE.getCodigo());

            cambiarDisponibilidadMedicoSegunEstadoCita(request.idMedico(),cita.getEstadoCita());

        }

    }

    @Transactional(readOnly = true)
    @Override
    public void validarCitasBloqueantesPaciente(Long idPaciente)
    {

        log.info("Buscando en citas si un paciente tiene citas en estado confirmada o en curso");

        if (citaRepository.existsByIdPacienteAndEstadoRegistroAndEstadoCitaIn(
                idPaciente,EstadoRegistro.ACTIVO,ESTADOS_CITA_BLOQUEANTE))
          throw  new EntidadRelacionadaException("El paciente no puede actualizarse o eliminarse porque tiene citas confirmadas o en curso");



    }

    @Transactional(readOnly = true)
    @Override
    public void validarCitasBloqueantesMedico(Long idMedico)
    {

        log.info("Buscando en citas si un medico tiene citas en estado confirmada o en curso");

        if (citaRepository.existsByIdMedicoAndEstadoRegistroAndEstadoCitaIn(
                idMedico,EstadoRegistro.ACTIVO,ESTADOS_CITA_BLOQUEANTE))
            throw  new EntidadRelacionadaException("El medico no puede actualizarse o eliminarse porque tiene citas confirmadas o en curso");



    }

    private Double calcularIMC(Double peso,Double estatura)
    {

        log.info("Calculando imc...");

        ValoresNumericosUtils.validarNumeroRequerido(peso);
        ValoresNumericosUtils.validarNumeroRequerido(estatura);

        if (peso < 0.1 || estatura < 1.0) throw new IllegalArgumentException("El peso no puede ser menor a 0.1 y la estatura no puede ser menor a 1.0");

        return peso / Math.pow(estatura, 2);

    }

    private String calcularNumeroExpediente (String numTelefono)
    {
        log.info("Generando numero de expediente");

        StringCustomUtils.validarTamanio(numTelefono,10,10,"El telefono debe contener exactamente 10 digitos");

        return numTelefono.chars()
                .mapToObj(digito -> (char) digito + "X")
                .collect(Collectors.joining());
    }






}
