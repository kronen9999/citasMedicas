package com.steven.msv.citas.mapper;


import com.steven.commons.dto.medicos.MedicoResponse;
import com.steven.commons.dto.medicos.datos.DatosMedico;
import com.steven.commons.dto.pacientes.PacienteRespose;
import com.steven.commons.dto.pacientes.datos.DatosPaciente;
import com.steven.commons.mapper.CommonMapper;
import com.steven.msv.citas.dto.CitaRequest;
import com.steven.msv.citas.dto.CitaResponse;
import com.steven.msv.citas.entity.Cita;
import org.springframework.stereotype.Component;

@Component
public class CitaMapper  implements CommonMapper<CitaRequest, CitaResponse, Cita> {


    @Override
    public Cita requestAEntidad(CitaRequest request) {
        if (request==null) return null;

        return Cita.crear(
          request.idPaciente(),
          request.idMedico(),
          request.fechaCita(),
          request.sintomas()
        );
    }

    @Override
    public CitaResponse entidadAResponse(Cita entidad) {
        if (entidad==null) return null;

        return new CitaResponse(
          entidad.getId(),
          null,
          null,
          entidad.getFechaCita(),
          entidad.getSintomas(),entidad.getEstadoCita().getDescripcion()
        );

    }


    public CitaResponse entidadAResponse(Cita entidad, PacienteRespose paciente, MedicoResponse medico) {
        if (entidad==null) return null;

        return new CitaResponse(
                entidad.getId(),
                pacienteResponseADatosPaciente(paciente),
                medicoResponseADatosMedico(medico),
                entidad.getFechaCita(),
                entidad.getSintomas(),entidad.getEstadoCita().getDescripcion()
        );

    }

    private DatosPaciente pacienteResponseADatosPaciente(PacienteRespose paciente){

        if (paciente==null) return null;

        return new DatosPaciente(
                paciente.nombre(),
                paciente.numExpediente(),
                paciente.email()+ "años",
                paciente.peso()+ "Kg",
                paciente.estatura()+ "m.",
                String.join(" ",Math.round(paciente.imc()*100.0)/100.0 + "",clasificacionIMC(paciente.imc())),
                paciente.telefono()
        );

    }

    private String clasificacionIMC(double imc)
    {
        if (imc <18.5) return  "Bajo peso";
        if (imc <25) return  "Peso normal";
        if (imc <30) return  "Sobrepeso";
        if (imc <35) return  "Obsesidad grado I";
        if (imc <40) return  "Obsesidad de grado II";
        return "Obsesidad de grado III";

    }

    private DatosMedico medicoResponseADatosMedico(MedicoResponse medico)
    {
        if (medico==null) return null;

        return new DatosMedico(
                 medico.nombre(),
                 medico.cedulaProfesional(),
                 medico.especialidad()
         );

    }




}
