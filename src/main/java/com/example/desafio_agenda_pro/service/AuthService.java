package com.example.desafio_agenda_pro.service;

import com.example.desafio_agenda_pro.domain.Usuario;
import com.example.desafio_agenda_pro.dto.auth.IniciarSesionRequest;
import com.example.desafio_agenda_pro.dto.auth.IniciarSesionResponse;
import com.example.desafio_agenda_pro.dto.auth.RegistrarUsuarioRequest;
import com.example.desafio_agenda_pro.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public void registrarse(RegistrarUsuarioRequest request) {

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));

        usuarioRepository.save(usuario);
    }

    public IniciarSesionResponse autenticar(IniciarSesionRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getNombre(),
                        request.getPassword()
                )
        );

        String token = jwtService.generarToken(authentication.getName());
        long expiraEn = jwtService.getTiempoExpiracion();

        return new IniciarSesionResponse(token, expiraEn);
    }
}
