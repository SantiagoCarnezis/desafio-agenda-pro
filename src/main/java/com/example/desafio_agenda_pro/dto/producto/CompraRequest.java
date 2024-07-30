package com.example.desafio_agenda_pro.dto.producto;

import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public class CompraRequest {

    private String medioPago;
    private int cantidad;
}
