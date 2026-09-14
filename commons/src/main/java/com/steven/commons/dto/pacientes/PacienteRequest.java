package com.steven.commons.dto.pacientes;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Datos de entrada para solicitudes de tipo post o put del modulo de pacientes")

public class PacienteRequest {

    @Schema(description = "Nombre del paciente", example = "Juan")
    @NotBlank(message = "El nombre no puede estar vacio")
    @Size(min = 1, max = 50, message = "El nombre debe contener entre 1 y 50 caracteres")
    String nombre;

    @Schema(description = "Apellido paterno del paciente", example = "Pérez")
    @NotBlank(message = "El apellido paterno no puede estar vacio")
    @Size(min = 1, max = 50, message = "El apellido paterno debe contener entre 1 y 50 caracteres")
    String apellidoPaterno;

    @Schema(description = "Apellido materno del paciente", example = "Gómez")
    @NotBlank(message = "El apellido materno no puede estar vacio")
    @Size(min = 1, max = 50, message = "El apellido materno debe contener entre 1 y 50 caracteres")
    String apellidoMaterno;

    @Schema(description = "Edad del paciente en anios", example = "30")
    @NotNull(message = "La edad no puede ser nula")
    @Min(value = 1, message = "La edad debe ser mayor o igual a 1")
    @Max(value = 100, message = "La edad no puede ser mayor a 100")
    Short edad;

    @Schema(description = "Peso del paciente en kilogramos", example = "75.5")
    @NotNull(message = "El peso no puede ser nulo")
    @DecimalMin(value = "0.1", message = "El peso debe ser mayor o igual a 0.1")
    @DecimalMax(value = "200.0", message = "El peso no puede ser mayor a 200")
    @Digits(integer = 3, fraction = 2, message = "El peso debe tener maximo 3 enteros y 2 decimales")
    Double peso;

    @Schema(description = "Estatura del paciente en metros", example = "1.75")
    @NotNull(message = "La estatura no puede ser nula")
    @DecimalMin(value = "1.0", message = "La estatura debe ser mayor o igual a 1.0")
    @DecimalMax(value = "2.0", message = "La estatura no puede ser mayor a 2.0")
    @Digits(integer = 1, fraction = 2, message = "La estatura debe tener maximo 1 entero y 2 decimales")
    Double estatura;

    @Schema(description = "Correo electronico del paciente", example = "juan.perez@gmail.com")
    @NotBlank(message = "El email no puede estar vacio")
    @Email(message = "El email debe tener un formato valido")
    @Size(max = 100, message = "El email debe contener maximo 100 caracteres")
    String email;

    @Schema(description = "Telefono del paciente de 10 digitos", example = "5512345678")
    @NotBlank(message = "El telefono no puede estar vacio")
    @Pattern(regexp = "^\\d{10}$", message = "El telefono debe contener exactamente 10 digitos")
    String telefono;

    @Schema(description = "Direccion del paciente", example = "Av. Reforma 123, Colonia Centro, Ciudad de México")
    @NotBlank(message = "La direccion no puede estar vacia")
    @Size(min = 1, max = 150, message = "La direccion debe contener entre 1 y 150 caracteres")
    String direccion;
}
