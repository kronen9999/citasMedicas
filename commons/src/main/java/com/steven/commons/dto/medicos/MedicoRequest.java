package com.steven.commons.dto.medicos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(
        description = "Datos requeridos para registrar un nuevo médico en el sistema"
)
public record MedicoRequest(

        @Schema(
                description = "Nombre del médico (entre 1 y 50 caracteres)",
                example = "María Fernanda"
        )

        @NotBlank(message = "El nombre es requerido")
        @Size (min=1,max = 50,message = "El nombre debe de tener entre 1 y 50 caracteres")
        String nombre,

        @Schema(
                description = "Apellido paterno del médico (entre 1 y 50 caracteres)",
                example = "García"
        )

        @NotBlank(message = "El apellido paterno  es requerido")
        @Size (min=1,max = 50,message = "El apellido paterno debe de tener entre 1 y 50 caracteres")
        String apellidoPaterno,

        @Schema(
                description = "Apellido materno del médico (entre 1 y 50 caracteres)",
                example = "López"
        )

        @NotBlank(message = "El apellido materno  es requerido")
        @Size (min=1,max = 50,message = "El apellido materno debe de tener entre 1 y 50 caracteres")
        String apellidoMaterno,

        @Schema(
                description = "Edad del médico. Debe estar entre 18 y 100 años",
                example = "34"
        )

        @NotNull(message = "La edad es requerida")
        @Min(value = 18,message = "La edad minima es de 18 años")
        @Max(value = 100,message = "La edad maxima es de 100 años")
        Short edad,

        @Schema(
                description = "Correo electrónico del médico con formato válido (correo@dominio), entre 1 y 100 caracteres",
                example = "maria.garcia@hospital.mx"
        )

        @NotBlank(message = "El email es requerido")
        @Size(min = 1,max = 100,message = "EL email debe de tener entre 1 y 100 caracteres")
        @Email(message = "El email debe de tener el formato correcto (correo@dominio)")
        String email,

        @Schema(
                description = "Número de teléfono del médico. Exactamente 10 dígitos numéricos",
                example = "5548219073"
        )

        @NotBlank(message = "El telefono es requerido")
        @Pattern(regexp = "^[0-9]{10}$",message = "EL telefono debe de contener solo  10 digitos numericos")
        String telefono,

        @Schema(
                description = "Cédula profesional del médico. Exactamente 12 caracteres",
                example = "ABC123456789"
        )
        @NotBlank(message = "La cedula profesional es requerido")
        @Size (min=12,max = 12,message = "La cedula profesional debe de tener exactamente 12 caracteres")
        String cedulaProfessional,

        @Schema(
                description = "Identificador de la especialidad a la que pertenece el médico. Debe ser un número positivo",
                example = "3"
        )
        @NotNull(message = "El ID de la especialidad es requerido")
        @Positive(message = "EL id de la especialidad debe de ser positivo")
        Long idEspecialidad

        ) { }
