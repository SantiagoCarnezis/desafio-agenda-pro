package com.example.desafio_agenda_pro.dto.estadisticas;

import com.example.desafio_agenda_pro.domain.Categoria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResumenVentasProducto {

    private String idProducto;
    private String nombre;
    private Categoria categoria;
    private Long cantidadVendida;
    private double totalRecaudado;

}
