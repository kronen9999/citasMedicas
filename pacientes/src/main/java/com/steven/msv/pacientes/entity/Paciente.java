package com.steven.msv.pacientes.entity;


import com.steven.commons.enums.EstadoRegistro;
import com.steven.commons.utils.StringCustomUtils;
import com.steven.commons.utils.ValoresNumericosUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "PACIENTES")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PACIENTE")
    Long id;

    @Column(name = "NOMBRE",nullable = false,length = 50)
    String nombre;

    @Column(name = "APELLIDO_PATERNO",nullable = false,length = 50)
    String apellidoPaterno;

    @Column(name = "APELLIDO_MATERNO",nullable = false,length = 50)
    String apellidoMaterno;

    @Column(name = "EDAD",nullable = false)
    Short edad;

    @Column(name = "PESO",nullable = false)
    Double peso;

    @Column(name = "ESTATURA",nullable = false)
    Double estatura;

    @Column(name = "IMC",nullable = false)
    Double imc;

    @Column(name = "EMAIL",nullable = false,length = 100)
    String email;

    @Column(name = "NUM_EXPEDIENTE",nullable = false,length = 20)
    String numExpediente;

    @Column(name = "TELEFONO",nullable = false,length = 10)
    String telefono;

    @Column(name = "DIRECCION",nullable = false,length = 150)
    String direccion;

    @Builder.Default
    @Column(name = "ESTADO_REGISTRO",nullable = false)
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    EstadoRegistro estadoRegistro=EstadoRegistro.ACTIVO;


    public void eliminarPaciente()
    {

        if (EstadoRegistro.ELIMINADO == this.estadoRegistro)
        {
            throw new IllegalStateException("El paciente ya se encuentra eliminado");
        }
        this.estadoRegistro = EstadoRegistro.ELIMINADO;
    }

    public void actualizarDatos(String nombre, String apellidoPaterno, String apellidoMaterno,
                                String email, String telefono, String direccion,
                                Short edad, Double peso, Double estatura,
                                Double imc, String numExpediente)
    {
        validarDatos(nombre, apellidoPaterno, apellidoMaterno, email, telefono, direccion, edad, peso, estatura);

        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.edad = edad;
        this.peso = peso;
        this.estatura = estatura;
        this.imc = imc;
        this.email = email;
        this.numExpediente = numExpediente;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    private void validarDatos(String nombre, String apellidoPaterno, String apellidoMaterno,
                              String email, String telefono, String direccion,
                              Short edad, Double peso, Double estatura)
    {
        StringCustomUtils.validarTamanio(nombre,1,50,"El nombre debe contener entre 1 y 50 caracteres");
        StringCustomUtils.validarTamanio(apellidoPaterno,1,50,"El apellido paterno debe contener entre 1 y 50 caracteres");
        StringCustomUtils.validarTamanio(apellidoMaterno,1,50,"El apellido materno debe contener entre 1 y 50 caracteres");
        StringCustomUtils.validarTamanio(email,1,100,"El email debe contener entre 1 y 100 caracteres");
        StringCustomUtils.validarTamanio(telefono,10,10,"El telefono debe contener exactamente 10 digitos");
        StringCustomUtils.validarTamanio(direccion,1,150,"La direccion debe contener entre 1 y 150 caracteres");

        ValoresNumericosUtils.validarNumeroRequerido(edad);
        ValoresNumericosUtils.validarNumeroRequerido(peso);
        ValoresNumericosUtils.validarNumeroRequerido(estatura);
    }







}
