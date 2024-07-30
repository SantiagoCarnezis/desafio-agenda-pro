package com.example.desafio_agenda_pro.controller;

import com.example.desafio_agenda_pro.domain.Producto;
import com.example.desafio_agenda_pro.dto.GenericResponse;
import com.example.desafio_agenda_pro.dto.Mensajes;
import com.example.desafio_agenda_pro.dto.producto.ActualizarProductoRequest;
import com.example.desafio_agenda_pro.dto.producto.CargarStockRequest;
import com.example.desafio_agenda_pro.dto.producto.CompraRequest;
import com.example.desafio_agenda_pro.dto.producto.CrearProductoRequest;
import com.example.desafio_agenda_pro.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class ProductoController {

    private final ProductoService productoService;

    @GetMapping
    public GenericResponse obtenerTodosLosProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = true) int offset) {

        List<Producto> productos;
        if(nombre == null) {

            productos = productoService.obtenerTodos(offset);
        }
        else {

            productos = productoService.obtenerTodosPorNombre(nombre, offset);
        }

        if (productos.isEmpty())
            return GenericResponse.responseExistosa(Mensajes.NO_HAY_PRODUCTOS_REGISTRADOS, productos);
        else
            return GenericResponse.responseExistosa(Mensajes.TODOS_LOS_PRODUCTOS, productos);
    }

    @GetMapping("/{codigo}")
    public GenericResponse obtenerProductoPorId(@PathVariable String codigo) {

        Producto producto = productoService.obtenerPorCodigo(codigo);

        return GenericResponse.responseExistosa(Mensajes.PRODUCTO_ENCONTRADO, producto);
    }

    @PostMapping("/comprar/{codigo}")
    public GenericResponse comprarProducto(@PathVariable String codigo, @RequestBody CompraRequest compra) {

        productoService.comprar(codigo, compra);

        return GenericResponse.responseExistosa(Mensajes.COMPRA_REALIZADA, null);
    }

    @PostMapping("/agregar/{codigo}")
    public GenericResponse agregarStock(@PathVariable String codigo, @RequestBody CargarStockRequest stock) {

        productoService.agrear(codigo, stock.getCantidad());

        return GenericResponse.responseExistosa(Mensajes.STOCK_CARGADO, null);
    }

    @PostMapping
    public GenericResponse crearProducto(@RequestBody CrearProductoRequest crearProducto) {

        Producto producto = productoService.crear(crearProducto);

        return GenericResponse.responseExistosa(Mensajes.PRODUCTO_CREADO, producto);
    }

    @PatchMapping("/{codigo}")
    public GenericResponse actualizarProducto(@PathVariable String codigo,
                                              @RequestBody ActualizarProductoRequest productoDetalles) {

        Producto productoActualizado = productoService.actualizar(codigo, productoDetalles);

        return GenericResponse.responseExistosa(Mensajes.PRODUCTO_ACTUALIZADO, productoActualizado);
    }

    @DeleteMapping("/{codigo}")
    public GenericResponse eliminarProducto(@PathVariable String codigo) {

        productoService.eliminar(codigo);

        return GenericResponse.responseExistosa(Mensajes.PRODUCTO_ELIMINADO, null);
    }
}
