package com.steven.pacientes.services;

import java.util.List;

public interface CommonCrud <RQ,RS>{

    List<RS> listar ();

    RS obtenerPorId(Long id);

    RS registrar(RQ request);

    RS actualizar (RQ request,Long id);

    RS editar (RQ request,Long id);

    void eliminar (Long id);


}
