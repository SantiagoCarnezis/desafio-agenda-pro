package com.example.desafio_agenda_pro.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@Table
public class HistorialPrecio {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotNull(message = "El codigo no puede ser nulo")
    private String codigoProducto;
    @Min(value = 0, message = "El precio no puede ser menor a 0.")
    private double precio;
    private LocalDateTime fechaActualizacion;
}
