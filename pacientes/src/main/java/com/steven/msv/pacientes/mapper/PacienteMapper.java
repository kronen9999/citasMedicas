package com.steven.msv.pacientes.mapper;

import com.steven.commons.dto.pacientes.PacienteRequest;
import com.steven.commons.dto.pacientes.PacienteRespose;
import com.steven.commons.mapper.CommonMapper;
import com.steven.msv.pacientes.entity.Paciente;
import org.springframework.stereotype.Component;


@Component
public class PacienteMapper implements CommonMapper<PacienteRequest, PacienteRespose, Paciente> {

    @Override
    public Paciente requestAEntidad(PacienteRequest request) {
        return Paciente.crear(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.email(),
                request.telefono(),
                request.direccion(),
                request.edad(),
                request.peso(),
                request.estatura()
        );
    }




    @Override
    public PacienteRespose entidadAResponse(Paciente entidad) {
        if (entidad==null) return null;

        return new PacienteRespose(entidad.getId(),
                String.join(" ",
                        entidad.getNombre(),
                        entidad.getApellidoPaterno(),
                        entidad.getApellidoMaterno()),
                entidad.getEdad(),
                entidad.getPeso(),
                entidad.getEstatura(),
                entidad.getImc(),
                entidad.getEmail(),
                entidad.getTelefono(),
                entidad.getDireccion(),
                entidad.getNumExpediente()
        );

    }
}
