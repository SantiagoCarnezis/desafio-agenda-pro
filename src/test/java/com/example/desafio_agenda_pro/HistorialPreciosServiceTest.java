package com.example.desafio_agenda_pro;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.LinkedList;
import java.util.UUID;

import com.example.desafio_agenda_pro.domain.Categoria;
import com.example.desafio_agenda_pro.domain.HistorialPrecio;
import com.example.desafio_agenda_pro.domain.Producto;
import com.example.desafio_agenda_pro.dto.estadisticas.EstadisticaPrecioDelProducto;
import com.example.desafio_agenda_pro.repository.HistorialPreciosRepository;
import com.example.desafio_agenda_pro.repository.ProductoRepository;
import com.example.desafio_agenda_pro.service.HistorialPreciosService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class HistorialPreciosServiceTest {

    @Mock
    private HistorialPreciosRepository historialPreciosRepository;
    @Mock
    private ProductoRepository productoRepository;
    @InjectMocks
    private HistorialPreciosService historialPreciosService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAgregarAlHistorial() {

        Producto producto = new Producto();
        String codigoProducto = UUID.randomUUID().toString();
        producto.setCodigo(codigoProducto);
        producto.setNombre("Producto1");
        producto.setCategoria(Categoria.CATEGORIA_A);
        producto.setPrecio(100.0);
        producto.cargarStock(100);

        historialPreciosService.agregar(producto);

        verify(historialPreciosRepository, times(1)).save(any(HistorialPrecio.class));
    }

    @Test
    public void testGetHistorialDePreciosDeProductoExistente() {

        String codigoProducto = UUID.randomUUID().toString();

        List<HistorialPrecio> historialPrecios = new LinkedList<>();

        HistorialPrecio historialPrecio = new HistorialPrecio();
        historialPrecio.setCodigoProducto(codigoProducto);
        historialPrecio.setPrecio(100.0);
        historialPrecio.setFechaActualizacion(LocalDateTime.now());

        HistorialPrecio historialPrecio2 = new HistorialPrecio();
        historialPrecio2.setCodigoProducto(codigoProducto);
        historialPrecio2.setPrecio(100.0);
        historialPrecio2.setFechaActualizacion(LocalDateTime.now().plusMinutes(5));

        historialPrecios.add(historialPrecio);
        historialPrecios.add(historialPrecio2);

        when(productoRepository.existsById(codigoProducto)).thenReturn(true);
        when(historialPreciosRepository.findAllByCodigoProductoOrderByFechaActualizacionDesc(codigoProducto)).thenReturn(historialPrecios);

        EstadisticaPrecioDelProducto result = historialPreciosService.getHistorialPrecio(codigoProducto);

        assertNotNull(result);
        assertEquals(codigoProducto, result.getCodigoProducto());
        assertTrue(result.isExiste());
        assertEquals(2, result.getEstadisticasPrecios().size());
        assertNotNull(result.getEstadisticasPrecios().get(0).getFechaFin());
        assertNull(result.getEstadisticasPrecios().get(1).getFechaFin());
        verify(productoRepository, times(1)).existsById(codigoProducto);
        verify(historialPreciosRepository, times(1)).findAllByCodigoProductoOrderByFechaActualizacionDesc(codigoProducto);
    }

    @Test
    public void testGetHistorialDePreciosDeProductoEliminado() {

        String codigoProducto = UUID.randomUUID().toString();

        List<HistorialPrecio> historialPrecios = new LinkedList<>();

        HistorialPrecio historialPrecio = new HistorialPrecio();
        historialPrecio.setCodigoProducto(codigoProducto);
        historialPrecio.setPrecio(100.0);
        historialPrecio.setFechaActualizacion(LocalDateTime.now());

        historialPrecios.add(historialPrecio);

        when(productoRepository.existsById(codigoProducto)).thenReturn(false);
        when(historialPreciosRepository.findAllByCodigoProductoOrderByFechaActualizacionDesc(codigoProducto)).thenReturn(historialPrecios);

        EstadisticaPrecioDelProducto result = historialPreciosService.getHistorialPrecio(codigoProducto);

        assertNotNull(result);
        assertEquals(codigoProducto, result.getCodigoProducto());
        assertFalse(result.isExiste());
        assertEquals(1, result.getEstadisticasPrecios().size());
        verify(productoRepository, times(1)).existsById(codigoProducto);
        verify(historialPreciosRepository, times(1)).findAllByCodigoProductoOrderByFechaActualizacionDesc(codigoProducto);
    }
}

