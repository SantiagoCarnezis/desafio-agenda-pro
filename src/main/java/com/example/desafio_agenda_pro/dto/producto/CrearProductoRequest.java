package com.example.desafio_agenda_pro.dto.producto;

import com.example.desafio_agenda_pro.domain.Categoria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrearProductoRequest {

    private String nombre;
    private Categoria categoria;
    private double precio;
    private int stock;
}
