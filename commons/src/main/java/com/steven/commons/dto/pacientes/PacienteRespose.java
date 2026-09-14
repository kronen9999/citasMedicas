package com.steven.commons.dto.pacientes;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "Datos de salida del modulo de pacientes")

public class PacienteRespose {

    @Schema(description = "Identificador unico del paciente", example = "1")
    Long id;

    @Schema(description = "Nombre completo del paciente", example = "Juan Pérez Gómez")
    String nombre;

    @Schema(description = "Edad del paciente en anios", example = "30")
    Short edad;

    @Schema(description = "Peso del paciente en kilogramos", example = "75.5")
    Double peso;

    @Schema(description = "Estatura del paciente en metros", example = "1.75")
    Double estatura;

    @Schema(description = "Indice de masa corporal calculado", example = "24.653061224489797")
    Double imc;

    @Schema(description = "Correo electronico del paciente", example = "juan.perez@gmail.com")
    String email;

    @Schema(description = "Telefono del paciente de 10 digitos", example = "5512345678")
    String telefono;

    @Schema(description = "Direccion del paciente", example = "Av. Reforma 123, Colonia Centro, Ciudad de México")
    String direccion;

    @Schema(description = "Numero de expediente del paciente", example = "5X5X1X2X3X4X5X6X7X8X")
    String numExpediente;
}
