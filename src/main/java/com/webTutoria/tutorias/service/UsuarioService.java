package com.webTutoria.tutorias.service;

import com.webTutoria.tutorias.model.Usuario;
import com.webTutoria.tutorias.repositorio.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    /**
     * Registra un nuevo usuario. Si el email ya existe, lanza una excepción.
     */
    public Usuario registrar(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe una cuenta con ese correo.");
        }
        if (usuario.getRol() == null) {
            usuario.setRol(Usuario.Rol.ALUMNO);
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    /**
     * Valida las credenciales del usuario. Si son correctas, devuelve el usuario;
     * si no, lanza una excepción.
     */
    public Usuario login(String email, String rawPassword) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Correo o contraseña incorrectos."));

        if (!passwordEncoder.matches(rawPassword, usuario.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Correo o contraseña incorrectos.");
        }
        return usuario;
    }

    /**
     * Genera un token de recuperación y envía el email al usuario.
     */
    public void solicitarRecuperacion(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe ninguna cuenta con ese correo."));

        // Genera un token único
        String token = UUID.randomUUID().toString();
        usuario.setResetToken(token);
        usuario.setTokenExpiry(LocalDateTime.now().plusHours(1)); // expira en 1 hora
        usuarioRepository.save(usuario);

        // Envía el email con el enlace
        emailService.enviarRecuperacion(email, token);
    }

    /**
     * Valida el token y actualiza la contraseña.
     */
    public void resetearContraseña(String token, String nuevaContraseña) {
        Usuario usuario = usuarioRepository.findByResetToken(token)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Token inválido o expirado."));

        if (usuario.getTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El enlace ha expirado.");
        }

        usuario.setPassword(passwordEncoder.encode(nuevaContraseña));
        usuario.setResetToken(null);
        usuario.setTokenExpiry(null);
        usuarioRepository.save(usuario);
    }
}
