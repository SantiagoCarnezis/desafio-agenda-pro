package com.example.desafio_agenda_pro.repository;

import com.example.desafio_agenda_pro.domain.Producto;
import com.example.desafio_agenda_pro.dto.estadisticas.UltimoProductoVendido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, String> {

    Page<Producto> findAllByNombreStartsWith(String nombre, Pageable pageable);

    @Modifying
    @Query("SELECT new com.example.desafio_agenda_pro.dto.estadisticas.UltimoProductoVendido(p.codigo, p.nombre, p.categoria, p.precio) " +
            "FROM Producto p " +
            "WHERE p.codigo IN :codigos ")
    List<UltimoProductoVendido> findCantidadVentasProducto(@Param("codigos") List<String> codigosProductos);
}
