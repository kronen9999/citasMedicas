package com.steven.servicio_a.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("servicio-b")
public interface ServicioBClient {

    @GetMapping
    String obtenerMensaje();
}
