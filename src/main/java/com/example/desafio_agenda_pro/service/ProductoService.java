package com.example.desafio_agenda_pro.service;

import com.example.desafio_agenda_pro.domain.Producto;
import com.example.desafio_agenda_pro.dto.producto.ActualizarProductoRequest;
import com.example.desafio_agenda_pro.dto.producto.CompraRequest;
import com.example.desafio_agenda_pro.dto.producto.CrearProductoRequest;
import com.example.desafio_agenda_pro.excepcion.ProductoNoEncontradoException;
import com.example.desafio_agenda_pro.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final VentaService ventaService;
    private final HistorialPreciosService historialPreciosService;
    private final int PAGE_SIZE = 20;

    public List<Producto> obtenerTodos(int offset) {
        return productoRepository.findAll(PageRequest.of(offset,PAGE_SIZE)).getContent();
    }

    public List<Producto> obtenerTodosPorNombre(String nombre, int offset) {
        return productoRepository.findAllByNombreStartsWith(nombre, PageRequest.of(offset,PAGE_SIZE)).getContent();
    }

    public Producto obtenerPorCodigo(String codigo) {
        return productoRepository.findById(codigo).orElseThrow(() -> new ProductoNoEncontradoException(codigo));
    }

    public Producto crear(CrearProductoRequest crearProducto) {

        Producto producto = new Producto();
        producto.setNombre(crearProducto.getNombre());
        producto.setCategoria(crearProducto.getCategoria());
        producto.setPrecio(crearProducto.getPrecio());
        producto.cargarStock(crearProducto.getStock());

        producto = productoRepository.save(producto);
        historialPreciosService.agregar(producto);

        return producto;
    }

    public void comprar(String codigoProducto, CompraRequest compra) {

        Producto producto = productoRepository.findById(codigoProducto).orElseThrow(() -> new ProductoNoEncontradoException(codigoProducto));
        producto.vender(compra.getCantidad());
        producto = productoRepository.save(producto);

        ventaService.crear(producto, compra);
    }

    public void agrear(String codigoProducto, int cantidad) {

        Producto producto = productoRepository.findById(codigoProducto).orElseThrow(() -> new ProductoNoEncontradoException(codigoProducto));
        producto.cargarStock(cantidad);
        productoRepository.save(producto);
    }

    public Producto actualizar(String codigo, ActualizarProductoRequest productoDetalles) {

        Producto producto = productoRepository.findById(codigo)
                .orElseThrow(() -> new ProductoNoEncontradoException(codigo));

        boolean actualizarHistorialPrecios = !producto.getPrecio().equals(productoDetalles.getPrecio());

        producto.setNombre(productoDetalles.getNombre());
        producto.setCategoria(productoDetalles.getCategoria());
        producto.setPrecio(productoDetalles.getPrecio());

        producto = productoRepository.save(producto);

        if (actualizarHistorialPrecios)
            historialPreciosService.agregar(producto);

        return producto;
    }

    public void eliminar(String codigo) {

        productoRepository.deleteById(codigo);
    }
}
