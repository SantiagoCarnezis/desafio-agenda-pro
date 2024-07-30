package com.example.desafio_agenda_pro.excepcion;


import com.example.desafio_agenda_pro.dto.Mensajes;

public class ProductoNoEncontradoException extends RuntimeException {

    public ProductoNoEncontradoException(String codigo) {
        super(Mensajes.PRODUCTO_NO_ENCONTRADO + codigo);
    }
}
