package com.example.desafio_agenda_pro.service;

import com.example.desafio_agenda_pro.domain.HistorialPrecio;
import com.example.desafio_agenda_pro.domain.Producto;
import com.example.desafio_agenda_pro.dto.estadisticas.EstadisticaPrecio;
import com.example.desafio_agenda_pro.dto.estadisticas.EstadisticaPrecioDelProducto;
import com.example.desafio_agenda_pro.repository.HistorialPreciosRepository;
import com.example.desafio_agenda_pro.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;


@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class HistorialPreciosService {

    private final HistorialPreciosRepository historialPreciosRepository;
    private final ProductoRepository productoRepository;

    @Async
    public void agregar(Producto producto) {

        HistorialPrecio historialPrecio = new HistorialPrecio();
        historialPrecio.setCodigoProducto(producto.getCodigo());
        historialPrecio.setPrecio(producto.getPrecio());
        historialPrecio.setFechaActualizacion(LocalDateTime.now());

        historialPreciosRepository.save(historialPrecio);
    }

    public EstadisticaPrecioDelProducto getHistorialPrecio(String codigoProducto) {

        boolean existe = productoRepository.existsById(codigoProducto);
        List<HistorialPrecio> historialPrecios = historialPreciosRepository.findAllByCodigoProductoOrderByFechaActualizacionDesc(codigoProducto);

        List<EstadisticaPrecio> estadisticas = new LinkedList<>();
        EstadisticaPrecio estadistica;
        HistorialPrecio historialPrecioAux;

        for (int i=0; i < historialPrecios.size(); i ++) {

            historialPrecioAux = historialPrecios.get(i);
            estadistica = new EstadisticaPrecio();
            estadistica.setPrecio(historialPrecioAux.getPrecio());
            estadistica.setFechaInicio(historialPrecioAux.getFechaActualizacion());

            if (i+1 < historialPrecios.size()) {

                historialPrecioAux = historialPrecios.get(i+1);
                estadistica.setFechaFin(historialPrecioAux.getFechaActualizacion());
            }
            else
                estadistica.setFechaFin(null);

            estadisticas.add(estadistica);
        }

        EstadisticaPrecioDelProducto estadisticaPrecioDelProducto = new EstadisticaPrecioDelProducto();
        estadisticaPrecioDelProducto.setCodigoProducto(codigoProducto);
        estadisticaPrecioDelProducto.setExiste(existe);
        estadisticaPrecioDelProducto.setEstadisticasPrecios(estadisticas);

        return estadisticaPrecioDelProducto;
    }
}
