package com.example.desafio_agenda_pro.dto.estadisticas;

import com.example.desafio_agenda_pro.domain.Categoria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UltimoProductoVendido {

    private String codigo;
    private String nombre;
    private Categoria categoria;
    private Double precioActual;

}
