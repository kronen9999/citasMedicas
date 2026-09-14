package com.steven.servicio_b.controller;

import com.steven.servicio_b.client.ServicioAClient;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class MensajeController {

    private final ServicioAClient servicioAClient;

    public MensajeController(ServicioAClient servicioAClient) {
        this.servicioAClient = servicioAClient;
    }

    @GetMapping("")
    public ResponseEntity<String> mensaje() {
        return ResponseEntity.ok("Hola desde el servicio b");
    }

    @GetMapping("/servicio-a")
    public ResponseEntity<String> consumirA() {
        return ResponseEntity.ok("Servicio b dice "+servicioAClient.obtenerSaludo());
    }
}