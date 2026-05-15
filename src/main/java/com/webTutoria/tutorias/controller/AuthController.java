package com.webTutoria.tutorias.controller;

import com.webTutoria.tutorias.dto.AuthDTO;
import com.webTutoria.tutorias.model.Usuario;
import com.webTutoria.tutorias.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@RequestBody AuthDTO.RegistroRequest req) {
        try {
            Usuario nuevo = new Usuario();
            nuevo.setNombre(req.nombre);
            nuevo.setEmail(req.email);
            nuevo.setPassword(req.password);
            nuevo.setRol(Usuario.Rol.ALUMNO);

            Usuario guardado = usuarioService.registrar(nuevo);
            return ResponseEntity.ok(toResponse(guardado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthDTO.LoginRequest req) {
        try {
            Usuario usuario = usuarioService.login(req.email, req.password);
            return ResponseEntity.ok(toResponse(usuario));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    private AuthDTO.AuthResponse toResponse(Usuario u) {
        return new AuthDTO.AuthResponse(
                u.getId(),
                u.getNombre(),
                u.getEmail(),
                u.getRol().name()
        );
    }
}