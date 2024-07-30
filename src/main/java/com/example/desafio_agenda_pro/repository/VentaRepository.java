package com.example.desafio_agenda_pro.repository;

import com.example.desafio_agenda_pro.domain.Venta;
import com.example.desafio_agenda_pro.dto.estadisticas.UltimoProductoVendido;
import com.example.desafio_agenda_pro.dto.estadisticas.ResumenVentasProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    @Query(value = "SELECT v.codigo_producto FROM venta v GROUP BY v.codigo_producto ORDER BY MAX(v.fecha) DESC LIMIT 3"
            , nativeQuery = true)
    List<String> findTop3ByDistinctByCodigoProducto();

    @Query("SELECT new com.example.desafio_agenda_pro.dto.estadisticas.ResumenVentasProducto(p.codigo, p.nombre, p.categoria, SUM(v.cantidad), SUM(v.precioTotal)) " +
            "FROM Producto p JOIN Venta v ON v.codigoProducto = p.codigo " +
            "WHERE p.codigo = :codigo " +
            "GROUP BY p.codigo, p.nombre, p.categoria")
    ResumenVentasProducto findResumenVentasProducto(@Param("codigo") String codigoProducto);

    List<Venta> findAllByCodigoProducto(String codigo);
}
