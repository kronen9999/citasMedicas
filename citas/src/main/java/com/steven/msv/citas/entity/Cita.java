package com.steven.msv.citas.entity;


import com.steven.commons.enums.EstadoRegistro;
import com.steven.commons.utils.StringCustomUtils;
import com.steven.commons.utils.ValoresNumericosUtils;
import com.steven.msv.citas.enums.EstadoCita;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

@Entity
@Tag(name = "CITAS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder @Getter
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CITA")
    private Long id;

    @Column(name = "ID_PACIENTE",nullable = false)
    private  Long idPaciente;

    @Column(name = "ID_MEDICO",nullable = false)
    private  Long idMedico;

    @Column(name = "FECHA_CITA",nullable = false)
    private LocalDateTime fechaCita;

    @Column(name = "SINTOMAS",nullable = false,length = 500)
    private  String sintomas;

    @Column(name = "ESTADO_CITA",nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoCita estadoCita;

    @Column(name = "ESTADO_REGISTRO",nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoRegistro estadoRegistro;


    private  static void validarId(Long id,String campo)
    {
        ValoresNumericosUtils.validarLongPositivo(id,"EL id del "+ campo +" es requerido y debe de ser positivo");

    }

    private  static void validarFecha(LocalDateTime fechaCita)
    {
        if (fechaCita==null||!fechaCita.isAfter(LocalDateTime.now()))
            throw  new IllegalArgumentException("La fecha de la cita es requerida y debe ser futura");

    }

    private  void validarNoEliminada()
    {
       if (this.estadoRegistro==EstadoRegistro.ELIMINADO)
           throw  new IllegalArgumentException("La cita ya esta eliminada");

    }

    public static void validarDatos (
            Long idPaciente,Long idMedico,
            LocalDateTime fechaCita,String sintomas)
    {

        validarId(idPaciente,"paciente");

        validarId(idMedico,"medico");

        validarFecha(fechaCita);

        StringCustomUtils.validarTamanio(sintomas,20,500,
                "Los sintomas son requeridos y deben estar entre 20 y 500 caracteres");
    }

    private void validarEliminacionPermitida()
    {

        validarNoEliminada();

        if (!estadoCita.isEliminable())
            throw  new IllegalStateException(
                    "La cita con estado "+ estadoCita + "no puede eliminarse");

    }

    private void validarActualizacionPermitida()
    {

        validarNoEliminada();

        if (!estadoCita.isActualizable())
            throw  new IllegalStateException(
                    "La cita con estado "+ estadoCita + "no puede actualizarse");

    }






    public void actualizar (
            Long idPaciente,Long idMedico,
            LocalDateTime fechaCita,String sintomas)
    {

         validarDatos(idPaciente,idMedico,fechaCita,sintomas);

        this.idPaciente=idPaciente;
        this.idMedico=idMedico;
        this.fechaCita=fechaCita;
        this.sintomas=sintomas.trim();
    }

    public static Cita crear (
            Long idPaciente,Long idMedico,
            LocalDateTime fechaCita,String sintomas
    ){

        validarDatos(idPaciente,idMedico,fechaCita,sintomas);

        return Cita.builder()
                .idPaciente(idPaciente)
                .idMedico(idMedico)
                .fechaCita(fechaCita)
                .sintomas(sintomas.trim())
                .estadoCita(EstadoCita.PENDIENTE)
                .estadoRegistro(EstadoRegistro.ACTIVO)
                .build();

    }

    public void actualizarEstadoCita(EstadoCita nuevoEstado)
    {
        validarActualizacionPermitida();

        if (nuevoEstado==null)
            throw  new IllegalArgumentException("El nuevo esto de la cita es requerido");

        if (!estadoCita.puedeCambiarA(nuevoEstado))
            throw  new IllegalStateException("La cita con estado "+
                    estadoCita + "solo puede cambiar a: "+estadoCita.puedeCambiar());

        this.estadoCita = nuevoEstado;
    }

    public void eliminar ()
    {

        validarEliminacionPermitida();

        this.estadoRegistro=EstadoRegistro.ELIMINADO;

    }







}
