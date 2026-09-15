package com.steven.msv.pacientes.mapper;

import com.steven.commons.dto.pacientes.PacienteRequest;
import com.steven.commons.dto.pacientes.PacienteRespose;
import com.steven.commons.enums.EstadoRegistro;
import com.steven.commons.mapper.CommonMapper;
import com.steven.msv.pacientes.entity.Paciente;
import org.springframework.stereotype.Component;


@Component
public class PacienteMapper implements CommonMapper<PacienteRequest, PacienteRespose, Paciente> {

    @Override
    public Paciente requestAEntidad(PacienteRequest request) {
        return requestAEntidad(request, null, null);
    }

    public Paciente requestAEntidad(PacienteRequest request, Double imc, String numExpediente)
    {
        return Paciente.builder()
                .nombre(request.nombre())
                .apellidoPaterno(request.apellidoPaterno())
                .apellidoMaterno(request.apellidoMaterno())
                .edad(request.edad())
                .peso(request.peso())
                .estatura(request.estatura())
                .imc(imc)
                .email(request.email())
                .numExpediente(numExpediente)
                .telefono(request.telefono())
                .direccion(request.direccion())
                .estadoRegistro(EstadoRegistro.ACTIVO)
                .build();
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
