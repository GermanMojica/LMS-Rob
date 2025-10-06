package com.primera.crud.service;

import com.primera.crud.model.Usuario;
import com.primera.crud.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario registrar(Usuario usuario) {
        usuario.setClave(passwordEncoder.encode(usuario.getClave())); // encriptar
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> login(String email, String password) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if (usuario.isPresent() && passwordEncoder.matches(password, usuario.get().getClave())) {
            return usuario;
        }
        return Optional.empty();
    }
}
