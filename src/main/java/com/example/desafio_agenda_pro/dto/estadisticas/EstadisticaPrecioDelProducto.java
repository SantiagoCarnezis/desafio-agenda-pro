package com.example.desafio_agenda_pro.dto.estadisticas;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
public class EstadisticaPrecioDelProducto {

    private String codigoProducto;
    private boolean existe;
    private List<EstadisticaPrecio> estadisticasPrecios;
}
