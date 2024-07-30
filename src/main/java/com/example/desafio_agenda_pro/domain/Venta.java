package com.example.desafio_agenda_pro.domain;

import com.example.desafio_agenda_pro.dto.producto.CompraRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String codigoProducto;
    @NotNull(message = "El medio de pago no puede ser nulo.")
    private String medioPago;
    @Min(value = 0, message = "La cantidad no puede ser menor a 0.")
    private int cantidad;
    private double precioUnidad;
    private double precioTotal;
    private LocalDateTime fecha;

    public Venta(Producto producto, CompraRequest compra) {

        this.codigoProducto = producto.getCodigo();
        this.medioPago = compra.getMedioPago();
        this.fecha = LocalDateTime.now();
        this.cantidad = compra.getCantidad();
        this.precioUnidad = producto.getPrecio();
        this.precioTotal = producto.getPrecio() * cantidad;
    }
}
