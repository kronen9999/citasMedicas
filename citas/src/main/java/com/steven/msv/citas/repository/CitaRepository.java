package com.steven.msv.citas.repository;

import com.steven.commons.enums.EstadoRegistro;
import com.steven.msv.citas.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CitaRepository extends JpaRepository<Cita,Long> {

    List <Cita> findByEstadoRegistro(EstadoRegistro estadoRegistro);

}
