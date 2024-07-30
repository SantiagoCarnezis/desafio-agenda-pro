package com.example.desafio_agenda_pro.repository;

import com.example.desafio_agenda_pro.domain.HistorialPrecio;
import com.example.desafio_agenda_pro.domain.Producto;
import com.example.desafio_agenda_pro.dto.estadisticas.EstadisticaPrecioDelProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface HistorialPreciosRepository extends JpaRepository<HistorialPrecio, Long> {


    List<HistorialPrecio> findAllByCodigoProductoOrderByFechaActualizacionDesc(String codigoPorducto);
}
