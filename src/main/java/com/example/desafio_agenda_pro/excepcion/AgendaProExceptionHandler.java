package com.example.desafio_agenda_pro.excepcion;

import com.example.desafio_agenda_pro.dto.GenericResponse;
import com.example.desafio_agenda_pro.dto.Mensajes;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@ControllerAdvice
public class AgendaProExceptionHandler {

    @ExceptionHandler(value= {HttpMessageNotReadableException.class, MissingServletRequestParameterException.class})
    protected ResponseEntity<GenericResponse> solicitudInvalida(Exception ex) {

        GenericResponse response = GenericResponse.getRespuestaFallida(Mensajes.BODY_NO_VALIDO);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    @ExceptionHandler(value= ExpiredJwtException.class)
    protected ResponseEntity<GenericResponse> jwExpirado(ExpiredJwtException ex) {

        GenericResponse response = GenericResponse.getRespuestaFallida(Mensajes.SESION_EXPIRADA);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value= BadCredentialsException.class)
    protected ResponseEntity<GenericResponse> credencialesIncorrectas(BadCredentialsException ex) {

        GenericResponse response = GenericResponse.getRespuestaFallida(Mensajes.CREDENCIALES_INCORRECTAS);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value ={DataIntegrityViolationException.class, SQLIntegrityConstraintViolationException.class})
    protected ResponseEntity<GenericResponse> usuarioDuplicado(SQLIntegrityConstraintViolationException ex) {

        GenericResponse response = GenericResponse.getRespuestaFallida(Mensajes.REGISTRO_DUPLICADO);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value= ConstraintViolationException.class)
    protected ResponseEntity<GenericResponse> resourceAlreadyExists(ConstraintViolationException ex) {

        List<String> errors = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).toList();
        GenericResponse response = GenericResponse.getRespuestaFallida(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value= SQLException.class)
    protected ResponseEntity<GenericResponse> resourceAlreadyExists(SQLException ex) {

        GenericResponse response = GenericResponse.getRespuestaFallida(ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value= ProductoNoEncontradoException.class)
    protected ResponseEntity<GenericResponse> productoNoEncontrado(ProductoNoEncontradoException ex) {

        GenericResponse response = GenericResponse.getRespuestaFallida(ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value= StockNoValidoException.class)
    protected ResponseEntity<GenericResponse> stockNoValido(StockNoValidoException ex) {

        GenericResponse response = GenericResponse.getRespuestaFallida(ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(value= StockInsuficienteException.class)
    protected ResponseEntity<GenericResponse> stockFaltante(StockInsuficienteException ex) {

        GenericResponse response = GenericResponse.getRespuestaFallida(ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

//    @ExceptionHandler(value= RuntimeException.class)
//    protected ResponseEntity<GenericResponse> errorNoEsperado(RuntimeException ex) {
//
//        ex.printStackTrace();
//        GenericResponse response = new GenericResponse();
//        response.setCodigo(HttpStatus.BAD_REQUEST.value());
//        response.setErrors(List.of("El sistema fallo"));
//        response.setMensaje("Solicitud fallida");
//
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//    }

}