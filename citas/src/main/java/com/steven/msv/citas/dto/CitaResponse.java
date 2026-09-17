package com.steven.msv.citas.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.steven.commons.dto.medicos.datos.DatosMedico;
import com.steven.commons.dto.pacientes.datos.DatosPaciente;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;


@Schema(description = "Informacion de una cita medica regstrada")
public record CitaResponse
        (

                @Schema(description = "Identificador unico de la cita ",example = "1")
                Long id,

                @Schema(description = "Informracion del paciente asociado a la cita")
                DatosPaciente paciente,

                @Schema(description = "Informacion del medico que atendera la cita")
                DatosMedico medico,

                @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd/MM/yyyy HH:mm")
                LocalDateTime fechaCita,


                @Schema(
                       description = "Descipcion de los sintomas indicados del paciente",
                       example = "El paciente presenta dolor de cabeza intenso y fiebre desde hace dos dias"
                        )
                String sintomas,

                @Schema(description = "Estado actual de la cita",example = "Confirmada por el paciente")
                String estadoCita


        ){}
