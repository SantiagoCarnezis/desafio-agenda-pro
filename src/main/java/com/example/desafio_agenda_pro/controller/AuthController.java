package com.example.desafio_agenda_pro.controller;

import com.example.desafio_agenda_pro.dto.GenericResponse;
import com.example.desafio_agenda_pro.dto.Mensajes;
import com.example.desafio_agenda_pro.dto.auth.IniciarSesionRequest;
import com.example.desafio_agenda_pro.dto.auth.IniciarSesionResponse;
import com.example.desafio_agenda_pro.dto.auth.RegistrarUsuarioRequest;
import com.example.desafio_agenda_pro.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class AuthController {

    private final AuthService authenticationService;

    @PostMapping("/registrar")
    public GenericResponse register(@RequestBody RegistrarUsuarioRequest request) {

        authenticationService.registrarse(request);

        return GenericResponse.responseExistosa(Mensajes.USUARIO_REGISTRADO.toString(), null);
    }

    @PostMapping("/iniciar-sesion")
    public GenericResponse authenticate(@RequestBody IniciarSesionRequest request) {

        IniciarSesionResponse response = authenticationService.autenticar(request);

        return GenericResponse.responseExistosa(Mensajes.SESION_INICIADA.toString(), response);
    }
}
