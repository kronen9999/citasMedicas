package com.steven.msv.citas.repository;

import com.steven.commons.enums.EstadoRegistro;
import com.steven.msv.citas.entity.Cita;
import com.steven.msv.citas.enums.EstadoCita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita,Long> {

    List <Cita> findByEstadoRegistro(EstadoRegistro estadoRegistro);

    boolean existsByIdPacienteAndEstadoRegistroAndEstadoCitaIn(Long id, EstadoRegistro estadoRegistro, Collection<EstadoCita> estadoCitas);

}
