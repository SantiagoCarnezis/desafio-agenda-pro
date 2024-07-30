package com.example.desafio_agenda_pro.controller;

import com.example.desafio_agenda_pro.domain.Venta;
import com.example.desafio_agenda_pro.dto.GenericResponse;
import com.example.desafio_agenda_pro.dto.Mensajes;
import com.example.desafio_agenda_pro.dto.estadisticas.EstadisticaPrecioDelProducto;
import com.example.desafio_agenda_pro.dto.estadisticas.UltimoProductoVendido;
import com.example.desafio_agenda_pro.dto.estadisticas.ResumenVentasProducto;
import com.example.desafio_agenda_pro.service.HistorialPreciosService;
import com.example.desafio_agenda_pro.service.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estadisticas")
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class EstadisticaController {

    private final VentaService ventaService;
    private final HistorialPreciosService historialPreciosService;

    @GetMapping("/ventas-producto/{codigo_producto}")
    public GenericResponse obtenerTodos(@PathVariable("codigo_producto") String codigoProducto) {

        List<Venta> ventas = ventaService.obtenerTodos(codigoProducto);

        return GenericResponse.responseExistosa(Mensajes.VENTAS_PRODUCTO, ventas);
    }

    @GetMapping("/ventas-producto/ultimos")
    public GenericResponse obtenerProductosVendidosRecientemente() {

        List<UltimoProductoVendido> ultimosProductoVendidos = ventaService.productosVendidosRecientemente();

        return GenericResponse.responseExistosa(Mensajes.ULTIMOS_PRODUCTOS_VENDIDOS, ultimosProductoVendidos);
    }

    @GetMapping("/ventas-producto/{codigo_producto}/resumen")
    public GenericResponse obtenerResumenVentasDelProducto(@PathVariable("codigo_producto") String codigoProducto) {

        ResumenVentasProducto resumenVentasProducto = ventaService.resumenVentasDelProducto(codigoProducto);

        return GenericResponse.responseExistosa(Mensajes.RESUMEN_VENTAS_PRODUCTO, resumenVentasProducto);
    }

    @GetMapping("/historial-precios/{codigo_producto}")
    public GenericResponse obtenerHistorialPrecios(@PathVariable("codigo_producto") String codigoProducto) {

        EstadisticaPrecioDelProducto historialPreciosDelProducto = historialPreciosService.getHistorialPrecio(codigoProducto);

        return GenericResponse.responseExistosa(Mensajes.HISTORIAL_PRECIOS_PRODUCTO, historialPreciosDelProducto);

    }
}
