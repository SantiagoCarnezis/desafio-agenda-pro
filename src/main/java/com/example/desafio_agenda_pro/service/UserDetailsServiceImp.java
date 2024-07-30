package com.example.desafio_agenda_pro.service;

import com.example.desafio_agenda_pro.dto.Mensajes;
import com.example.desafio_agenda_pro.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class UserDetailsServiceImp implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return usuarioRepository.findByNombre(username)
                .orElseThrow(() -> new UsernameNotFoundException(Mensajes.USUARIO_NO_ENCONTRADO));
    }
}
