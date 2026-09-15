package com.steven.commons.dto.medicos.datos;

import io.swagger.v3.oas.annotations.media.Schema;

public record DatosMedico(

        @Schema(description = "Nombre completo del medico",example = "Christian Garcia Lopez")
        String nombre,

        @Schema(description = "Cedula profesional del medico",example = "123456789012")
        String cedulaProfesional,

        @Schema(description = "Nombre de la especialidad medica del medico",example = "Cardiologia")
        String especialidad

) {}
