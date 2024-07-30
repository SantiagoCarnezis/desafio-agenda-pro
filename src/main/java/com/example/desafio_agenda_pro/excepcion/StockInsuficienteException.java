package com.example.desafio_agenda_pro.excepcion;


import com.example.desafio_agenda_pro.dto.Mensajes;

public class StockInsuficienteException extends RuntimeException {

    public StockInsuficienteException(String codigo) {
        super(Mensajes.STOCK_INSUFICIENTE + codigo);
    }
}
