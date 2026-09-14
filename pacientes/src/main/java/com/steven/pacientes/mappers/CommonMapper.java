package com.steven.pacientes.mappers;

public interface CommonMapper <RQ,RS,E>{

    E responseAEntidad(RQ request);

    RS entidadAResponse(E entidad);

}
