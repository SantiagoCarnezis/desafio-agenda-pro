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
public class ActualizarProductoRequest {

    private String nombre;
    private Categoria categoria;
    private Double precio;
}
