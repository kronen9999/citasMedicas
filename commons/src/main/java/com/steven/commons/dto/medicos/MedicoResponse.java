package com.steven.commons.dto.medicos;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        description = "Datos del medico retornados por el sistema"
)
public record MedicoResponse(

        @Schema(
                description = "Identificador unico del medico",
                example = "1"
        )
        Long id,

        @Schema(
                description = "Nombre del medico",
                example = "Fernanda"
        )
        String nombre,

        @Schema(
                description = "Edad del medico",
                example = "34"
        )
        Short edad,

        @Schema(
                description = "Correo electronico del medico",
                example = "maria.garcia@hospital.mx"
        )
        String email,

        @Schema(
                description = "Numero de telefono del mÃ©dico (10 digitos)",
                example = "5548219073"
        )
        String telefono,

        @Schema(
                description = "Cedula profesional del mÃ©dico (12 caracteres)",
                example = "ABC123456789"
        )
        String cedulaProfesional,

        @Schema(
                description = "Nombre de la especialidad del mÃ©dico",
                example = "CardiologÃ­a"
        )
        String especialidad,

        @Schema(
                description = "DescripciÃ³n de la disponibilidad del mÃ©dico",
                example = "Lunes a Viernes 09:00-15:00"
        )
        String disponibilidad,

        @Schema(
                description = "Identificador de la disponibilidad asociada al mÃ©dico",
                example = "2"
        )
        Long idDisponibilidad
) {}
