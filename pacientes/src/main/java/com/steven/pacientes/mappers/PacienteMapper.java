package com.steven.pacientes.mappers;


import com.steven.pacientes.enums.EstadoRegistro;
import com.steven.pacientes.dto.paciente.PacienteRequest;
import com.steven.pacientes.dto.paciente.PacienteRespose;
import com.steven.pacientes.entities.Paciente;
import org.springframework.stereotype.Component;

@Component
public class PacienteMapper  implements CommonMapper<PacienteRequest, PacienteRespose, Paciente> {

    @Override
    public Paciente responseAEntidad(PacienteRequest request) {
        return responseAEntidad(request, null, null);
    }

    public Paciente responseAEntidad(PacienteRequest request,Double imc,String numExpediente)
    {
        return Paciente.builder()
                .Nombre(request.getNombre())
                .apellidoPaterno(request.getApellidoPaterno())
                .apellidoMaterno(request.getApellidoMaterno())
                .edad(request.getEdad())
                .peso(request.getPeso())
                .estatura(request.getEstatura())
                .imc(imc)
                .email(request.getEmail())
                .numExpediente(numExpediente)
                .telefono(request.getTelefono())
                .direccion(request.getDireccion())
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
