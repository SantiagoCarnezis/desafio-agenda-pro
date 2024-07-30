package com.example.desafio_agenda_pro.service;

import com.example.desafio_agenda_pro.domain.Producto;
import com.example.desafio_agenda_pro.domain.Venta;
import com.example.desafio_agenda_pro.dto.estadisticas.UltimoProductoVendido;
import com.example.desafio_agenda_pro.dto.estadisticas.ResumenVentasProducto;
import com.example.desafio_agenda_pro.dto.producto.CompraRequest;
import com.example.desafio_agenda_pro.repository.ProductoRepository;
import com.example.desafio_agenda_pro.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;

    public List<Venta> obtenerTodos(String codigoProducto) {
        return ventaRepository.findAllByCodigoProducto(codigoProducto);
    }

    @Async
    public void crear(Producto producto, CompraRequest compra) {

        Venta venta = new Venta(producto, compra);
        ventaRepository.save(venta);
    }

    public List<UltimoProductoVendido> productosVendidosRecientemente() {

        List<String> idUltimosProductosVendidos = ventaRepository.findTop3ByDistinctByCodigoProducto();
        return productoRepository.findCantidadVentasProducto(idUltimosProductosVendidos);
    }

    public ResumenVentasProducto resumenVentasDelProducto(String idProducto) {

        return ventaRepository.findResumenVentasProducto(idProducto);
    }
}
