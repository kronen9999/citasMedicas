package com.steven.servicio_a.controller;


import com.steven.servicio_a.client.ServicioBClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")

public class SaludoController {

    private final ServicioBClient servicioBClient;

    public SaludoController(ServicioBClient servicioBClient) {
        this.servicioBClient = servicioBClient;
    }



    @GetMapping("/servicio-b")
    public ResponseEntity<String> consumirB ()
    {
        return  ResponseEntity.ok("Servicio a dice:"+ servicioBClient.obtenerMensaje());
    }

    @GetMapping("")
    public ResponseEntity<String> saludo ()
    {
        return  ResponseEntity.ok("Servicio a dice hola mundo");
    }
}

