package com.steven.msv.medicos.mapper;

import com.steven.commons.dto.medicos.MedicoRequest;
import com.steven.commons.dto.medicos.MedicoResponse;
import com.steven.commons.enums.DisponibilidadMedico;
import com.steven.commons.enums.EstadoRegistro;
import com.steven.commons.mapper.CommonMapper;
import com.steven.msv.medicos.entity.Medico;
import org.springframework.stereotype.Component;

@Component
public class MedicoMapper implements CommonMapper<MedicoRequest, MedicoResponse, Medico> {

    @Override
    public Medico requestAEntidad(MedicoRequest request) {
        if (request==null) return null;

        return  Medico.builder()
                .nombre(request.nombre().trim())
                .apellidoPaterno(request.apellidoPaterno().trim())
                .apellidoMaterno(request.apellidoMaterno().trim())
                .edad(request.edad())
                .email(request.email().trim().toLowerCase())
                .telefono(request.telefono().trim())
                .cedulaProfesional(request.cedulaProfesional().trim())
                .disponibilidad(DisponibilidadMedico.DISPONIBLE)
                .estadoRegistro(EstadoRegistro.ACTIVO).
                build();

    }

    @Override
    public MedicoResponse entidadAResponse(Medico entidad) {
        if (entidad==null) return null;

        return  new MedicoResponse(
                entidad.getId(),
                String.join(" ",entidad.getNombre(),
                        entidad.getApellidoPaterno(),
                        entidad.getApellidoMaterno()),
                entidad.getEdad(),
                entidad.getEmail(),
                entidad.getTelefono(),
                entidad.getCedulaProfesional(),
                entidad.getEspecialidad().getDescripcion(),
                entidad.getDisponibilidad().getDescripcion(),
        entidad.getDisponibilidad().getCodigo()
        );
    }
}
