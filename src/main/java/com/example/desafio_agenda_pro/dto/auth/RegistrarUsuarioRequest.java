package com.example.desafio_agenda_pro.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrarUsuarioRequest {

    private String nombre;
    private String password;
}
