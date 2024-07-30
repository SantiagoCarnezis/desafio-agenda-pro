package com.example.desafio_agenda_pro.excepcion;


import com.example.desafio_agenda_pro.dto.Mensajes;

public class StockNoValidoException extends RuntimeException {

    public StockNoValidoException() {
        super(Mensajes.STOCK_NO_VALIDO);
    }
}
