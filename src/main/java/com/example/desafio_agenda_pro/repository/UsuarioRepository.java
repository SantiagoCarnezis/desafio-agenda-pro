package com.example.desafio_agenda_pro.repository;

import com.example.desafio_agenda_pro.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

    Optional<Usuario> findByNombre(String nombre);
}
