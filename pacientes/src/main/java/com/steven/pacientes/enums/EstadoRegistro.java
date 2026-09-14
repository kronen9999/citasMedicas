package com.steven.pacientes.enums;

import com.steven.pacientes.exceptions.RecursoNoEncontradoException;
import com.steven.pacientes.utils.StringCustomUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Getter
public enum EstadoRegistro {
    ACTIVO("Activo"),
    ELIMINADO("Eliminado");


    private final String descripcion;

    public static EstadoRegistro obtenerEstadoRegistroPorDescripcion(String descripcion) {
        StringCustomUtils.validarNoVacio(descripcion, "La descripcion es requerida");

        String descripcionNormalizada = StringCustomUtils.quitarAcentos(descripcion);


        for (EstadoRegistro estadoRegistro : values()) {
            if (StringCustomUtils.quitarAcentos(estadoRegistro.descripcion).equalsIgnoreCase(descripcionNormalizada)) {
                return estadoRegistro;
            }
        }

        throw new RecursoNoEncontradoException("No existe una categoria para la descripcion : " + descripcion);


    }

}


