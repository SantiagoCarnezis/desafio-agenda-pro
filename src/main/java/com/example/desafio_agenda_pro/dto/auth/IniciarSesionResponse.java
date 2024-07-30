package com.example.desafio_agenda_pro.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class IniciarSesionResponse {

    private String token;
    private long expiraEn;
}
