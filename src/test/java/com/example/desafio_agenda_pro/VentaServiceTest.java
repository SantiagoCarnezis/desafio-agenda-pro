package com.example.desafio_agenda_pro;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import com.example.desafio_agenda_pro.domain.Categoria;
import com.example.desafio_agenda_pro.domain.Producto;
import com.example.desafio_agenda_pro.domain.Venta;
import com.example.desafio_agenda_pro.dto.estadisticas.ResumenVentasProducto;
import com.example.desafio_agenda_pro.dto.estadisticas.UltimoProductoVendido;
import com.example.desafio_agenda_pro.dto.producto.CompraRequest;
import com.example.desafio_agenda_pro.excepcion.StockInsuficienteException;
import com.example.desafio_agenda_pro.repository.ProductoRepository;
import com.example.desafio_agenda_pro.repository.VentaRepository;
import com.example.desafio_agenda_pro.service.VentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;
    @Mock
    private ProductoRepository productoRepository;
    @InjectMocks
    private VentaService ventaService;
    private Producto producto1;
    private String codigoProducto1;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        codigoProducto1 = UUID.randomUUID().toString();

        producto1 = new Producto();
        producto1.setCodigo(codigoProducto1);
        producto1.setNombre("Producto1");
        producto1.setCategoria(Categoria.CATEGORIA_A);
        producto1.setPrecio(100.0);
        producto1.cargarStock(100);
    }

    @Test
    public void testObtenerVentasDelProducto() {

        List<Venta> ventas = new ArrayList<>();
        CompraRequest compra1 = new CompraRequest();
        compra1.setCantidad(10);
        compra1.setMedioPago("efectivo");

        CompraRequest compra2 = new CompraRequest();
        compra2.setCantidad(20);
        compra2.setMedioPago("efectivo");

        ventas.add(new Venta(producto1, compra1));
        ventas.add(new Venta(producto1, compra2));

        when(ventaRepository.findAllByCodigoProducto(codigoProducto1)).thenReturn(ventas);

        List<Venta> result = ventaService.obtenerTodos(codigoProducto1);

        assertEquals(2, result.size());
        verify(ventaRepository, times(1)).findAllByCodigoProducto(codigoProducto1);
    }

    @Test
    public void testRealizarVenta() {

        CompraRequest compra1 = new CompraRequest();
        compra1.setCantidad(10);
        compra1.setMedioPago("efectivo");

        ventaService.crear(producto1, compra1);

        verify(ventaRepository, times(1)).save(any(Venta.class));
    }

    @Test
    public void testProductosVendidosRecientementeTresComprasDosProductos() {

        List<String> idUltimosProductos = List.of("uuid1", "uuid2");
        List<UltimoProductoVendido> ultimosVendidos = new ArrayList<>();

        when(ventaRepository.findTop3ByDistinctByCodigoProducto()).thenReturn(idUltimosProductos);
        when(productoRepository.findCantidadVentasProducto(idUltimosProductos)).thenReturn(ultimosVendidos);

        List<UltimoProductoVendido> result = ventaService.productosVendidosRecientemente();

        assertEquals(0, result.size());
        verify(ventaRepository, times(1)).findTop3ByDistinctByCodigoProducto();
        verify(productoRepository, times(1)).findCantidadVentasProducto(idUltimosProductos);
    }

    @Test
    public void testResumenVentasDelProducto() {
        ResumenVentasProducto resumen = new ResumenVentasProducto();

        when(ventaRepository.findResumenVentasProducto(codigoProducto1)).thenReturn(resumen);

        ResumenVentasProducto result = ventaService.resumenVentasDelProducto(codigoProducto1);

        assertNotNull(result);
        verify(ventaRepository, times(1)).findResumenVentasProducto(codigoProducto1);
    }
}

