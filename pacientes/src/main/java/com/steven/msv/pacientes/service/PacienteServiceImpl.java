package com.steven.msv.pacientes.service;


import com.steven.commons.clients.CitaClient;
import com.steven.commons.dto.pacientes.PacienteRequest;
import com.steven.commons.dto.pacientes.PacienteRespose;
import com.steven.commons.enums.EstadoRegistro;
import com.steven.commons.exceptions.RecursoNoEncontradoException;
import com.steven.commons.utils.StringCustomUtils;
import com.steven.commons.utils.ValoresNumericosUtils;
import com.steven.msv.pacientes.entity.Paciente;
import com.steven.msv.pacientes.mapper.PacienteMapper;
import com.steven.msv.pacientes.repository.PacienteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Transactional
@Slf4j
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;

    private final PacienteMapper pacienteMapper;

    private final CitaClient citaClient;

    @Override
    @Transactional(readOnly = true)
    public List<PacienteRespose> listar() {
        log.info("Obteniendo informacion de los pacientes activos");

        return pacienteRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(pacienteMapper::entidadAResponse)
                .toList();
    }



    @Transactional(readOnly = true)
    @Override
    public PacienteRespose obtenerPorId(Long id) {
        log.info("Obteniendo informacion del paciente activo con id {}",id);

        return pacienteMapper.entidadAResponse(buscarPacienteActivoID(id));
    }

    @Transactional(readOnly = true)
    @Override
    public PacienteRespose obtenerPorIdSinValidarEstado(Long id) {
        log.info("Obteniendo informacion del paciente sin validar estado con id {}",id);

        return pacienteMapper.entidadAResponse(buscarPacienteID(id));
    }

    @Override
    public PacienteRespose registrar(PacienteRequest request) {
        log.info("Registrando paciente con email {}",request.email());

        validarDuplicados(request.email(),request.telefono());

        Double imc = calcularIMC(request.peso(), request.estatura());
        String numExpediente = calcularNumeroExpediente(request.telefono());

        Paciente paciente = pacienteMapper.requestAEntidad(request, imc, numExpediente);

        return pacienteMapper.entidadAResponse(pacienteRepository.save(paciente));
    }

    @Override
    public PacienteRespose actualizar(PacienteRequest request, long id) {
        log.info("Actualizando paciente con id {}",id);

        Paciente paciente = buscarPacienteActivoID(id);

        citaClient.validarExistenPacientesConfirmadasOCurso(id);

        validarDuplicadosEnActualizacion(request.email(),request.telefono(),id);

        Double imc = calcularIMC(request.peso(), request.estatura());
        String numExpediente = calcularNumeroExpediente(request.telefono());

        paciente.actualizarDatos(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.email(),
                request.telefono(),
                request.direccion(),
                request.edad(),
                request.peso(),
                request.estatura(),
                imc,
                numExpediente
        );

        return pacienteMapper.entidadAResponse(paciente);
    }

    @Override
    public void eliminar(Long id) {

        log.info("Iniciando eliminacion logica del paciente con id {}", id);

        Paciente paciente = buscarPacienteID(id);

        citaClient.validarExistenPacientesConfirmadasOCurso(id);

        paciente.eliminarPaciente();

        log.info("Se ha eliminado el paciente con id {}",id);

    }

    private void validarDuplicados(String email, String telefono)
    {
        if (pacienteRepository.existsByEmailAndEstadoRegistro(email, EstadoRegistro.ACTIVO))
        {
            throw new IllegalStateException("Ya existe un paciente activo con el email "+email);
        }

        if (pacienteRepository.existsByTelefonoAndEstadoRegistro(telefono, EstadoRegistro.ACTIVO))
        {
            throw new IllegalStateException("Ya existe un paciente activo con el telefono "+telefono);
        }
    }

    private void validarDuplicadosEnActualizacion(String email, String telefono, Long id)
    {
        if (pacienteRepository.existsByEmailAndEstadoRegistroAndIdNot(email, EstadoRegistro.ACTIVO, id))
        {
            throw new IllegalStateException("Ya existe otro paciente activo con el email "+email);
        }

        if (pacienteRepository.existsByTelefonoAndEstadoRegistroAndIdNot(telefono, EstadoRegistro.ACTIVO, id))
        {
            throw new IllegalStateException("Ya existe otro paciente activo con el telefono "+telefono);
        }
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

    private Paciente buscarPacienteID(Long id)
    {
        ValoresNumericosUtils.validarNumeroRequerido(id);

        log.info("Buscando paciente con id {}",id);

        return pacienteRepository.findById(id).orElseThrow(()-> new RecursoNoEncontradoException("No se ha encontrado el paciente con " +
                "id "+id));

    }

    private Paciente buscarPacienteActivoID(Long id)
    {
        ValoresNumericosUtils.validarNumeroRequerido(id);

        log.info("Buscando paciente activo con id {}",id);

        return pacienteRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO)
                .orElseThrow(()-> new RecursoNoEncontradoException("No se ha encontrado el paciente activo con " +
                        "id "+id));

    }


}
