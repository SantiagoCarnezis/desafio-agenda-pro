package com.example.desafio_agenda_pro;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.UUID;

import com.example.desafio_agenda_pro.domain.Categoria;
import com.example.desafio_agenda_pro.domain.Producto;
import com.example.desafio_agenda_pro.dto.producto.ActualizarProductoRequest;
import com.example.desafio_agenda_pro.dto.producto.CompraRequest;
import com.example.desafio_agenda_pro.dto.producto.CrearProductoRequest;
import com.example.desafio_agenda_pro.excepcion.ProductoNoEncontradoException;
import com.example.desafio_agenda_pro.excepcion.StockInsuficienteException;
import com.example.desafio_agenda_pro.excepcion.StockNoValidoException;
import com.example.desafio_agenda_pro.repository.ProductoRepository;
import com.example.desafio_agenda_pro.service.HistorialPreciosService;
import com.example.desafio_agenda_pro.service.ProductoService;
import com.example.desafio_agenda_pro.service.VentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;
    @Mock
    private VentaService ventaService;
    @Mock
    private HistorialPreciosService historialPreciosService;
    @InjectMocks
    private ProductoService productoService;
    private Producto producto;
    private String codigoProducto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        codigoProducto = UUID.randomUUID().toString();

        producto = new Producto();
        producto.setCodigo(codigoProducto);
        producto.setNombre("Producto1");
        producto.setCategoria(Categoria.CATEGORIA_A);
        producto.setPrecio(100.0);
        producto.cargarStock(100);

    }

    @Test
    public void testObtenerTodos() {

        List<Producto> productos = new ArrayList<>();
        productos.add(producto);

        when(productoRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(productos));

        List<Producto> result = productoService.obtenerTodos(0);

        assertEquals(1, result.size());
        verify(productoRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    public void testObtenerTodosPorNombre() {

        String codigoProducto2 = UUID.randomUUID().toString();
        Producto producto2 = new Producto();
        producto2.setCodigo(codigoProducto2);
        producto2.setNombre("Producto2");
        producto2.setCategoria(Categoria.CATEGORIA_A);
        producto2.setPrecio(100.0);
        producto2.cargarStock(100);

        List<Producto> productos = new ArrayList<>();
        productos.add(producto);
        productos.add(producto2);

        List<Producto> productosFiltradoPorNombre = productos.stream().filter(p -> p.getNombre().equals("Producto2")).toList();

        when(productoRepository.findAllByNombreStartsWith(eq("Producto2"), any(Pageable.class))).
                thenReturn(new PageImpl<>(productosFiltradoPorNombre));

        List<Producto> result = productoService.obtenerTodosPorNombre("Producto2", 0);

        assertEquals(1, result.size());
        verify(productoRepository, times(1)).
                findAllByNombreStartsWith(eq("Producto2"), any(Pageable.class));
    }

    @Test
    public void testObtenerPorCodigo() {

        when(productoRepository.findById(codigoProducto)).thenReturn(Optional.of(producto));

        Producto result = productoService.obtenerPorCodigo(codigoProducto);

        assertEquals(codigoProducto, result.getCodigo());
        verify(productoRepository, times(1)).findById(codigoProducto);
    }

    @Test
    public void testCrear() {

        CrearProductoRequest request = new CrearProductoRequest("Producto1", Categoria.CATEGORIA_A, 100.0, 100);

        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto result = productoService.crear(request);

        assertNotNull(result);
        assertEquals("Producto1", result.getNombre());
        verify(productoRepository, times(1)).save(any(Producto.class));
        verify(historialPreciosService, times(1)).agregar(any(Producto.class));
    }

    @Test
    public void testCrearProductoNoValido() {

        CrearProductoRequest request = new CrearProductoRequest("Producto1", Categoria.CATEGORIA_A, 100.0, -100);

        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        assertThrows(StockNoValidoException.class, () -> {productoService.crear(request);});
    }

    @Test
    public void testComprar() {

        CompraRequest compra = new CompraRequest();
        compra.setMedioPago("efectivo");
        compra.setCantidad(2);

        when(productoRepository.findById(codigoProducto)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        productoService.comprar(codigoProducto, compra);

        verify(productoRepository, times(1)).save(any(Producto.class));
        verify(ventaService, times(1)).crear(any(Producto.class), any(CompraRequest.class));
    }

    @Test
    public void testCompraFallidaStockInsuficiente() {

        CompraRequest compra = new CompraRequest();
        compra.setMedioPago("efectivo");
        compra.setCantidad(producto.getStock() + 1);

        when(productoRepository.findById(codigoProducto)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        assertThrows(StockInsuficienteException.class, () -> {productoService.comprar(codigoProducto, compra);});
    }

    @Test
    public void testCompraFallidaProductoNoEncontrado() {

        CompraRequest compra = new CompraRequest();
        compra.setMedioPago("efectivo");
        compra.setCantidad(1);

        assertThrows(ProductoNoEncontradoException.class, () -> {productoService.comprar(codigoProducto, compra);});
    }

    @Test
    public void testActualizar() {

        ActualizarProductoRequest request = new ActualizarProductoRequest("Producto1", Categoria.CATEGORIA_A, 150.0);

        when(productoRepository.findById(codigoProducto)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto);

        Producto result = productoService.actualizar(codigoProducto, request);

        assertEquals(150.0, result.getPrecio());
        verify(productoRepository, times(1)).save(any(Producto.class));
        verify(historialPreciosService, times(1)).agregar(any(Producto.class));
    }

    @Test
    public void testActualizacionFallidaProductoNoEncontrado() {

        ActualizarProductoRequest request = new ActualizarProductoRequest("Producto1", Categoria.CATEGORIA_A, 150.0);

        assertThrows(ProductoNoEncontradoException.class, () -> {productoService.actualizar(codigoProducto, request);});

    }

    @Test
    public void testEliminar() {

        productoService.eliminar(codigoProducto);

        verify(productoRepository, times(1)).deleteById(codigoProducto);
    }
}

