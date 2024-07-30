package com.example.desafio_agenda_pro.domain;

import com.example.desafio_agenda_pro.config.AtomicIntegerConverter;
import com.example.desafio_agenda_pro.excepcion.StockInsuficienteException;
import com.example.desafio_agenda_pro.excepcion.StockNoValidoException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
@Entity
@Table
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    //@UuidGenerator
    private String codigo;
    @NotNull(message = "El nombre no puede ser nulo.")
    //@Column(unique = true)
    private String nombre;
    @Enumerated(value = EnumType.ORDINAL)
    @NotNull(message = "La categoria no puede ser nulo.")
    private Categoria categoria;
    @Min(value = 0, message = "El precio no puede ser menor a 0.")
    private Double precio;
    @Convert(converter = AtomicIntegerConverter.class)
    private AtomicInteger stock;

    public Producto() {

        this.stock = new AtomicInteger(0);
    }

    public void vender(int cantidad) {

        int nuevoStock = stock.addAndGet(-cantidad);
        if (nuevoStock < 0)
            throw new StockInsuficienteException(codigo);
    }

    public void cargarStock(int cantidad) {
        if (cantidad < 0)
            throw new StockNoValidoException();
        stock.addAndGet(cantidad);
    }

    public int getStock() {
        return stock.get();
    }
}
