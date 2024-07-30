package com.example.desafio_agenda_pro.dto.estadisticas;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class EstadisticaPrecio {

    private double precio;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
}
