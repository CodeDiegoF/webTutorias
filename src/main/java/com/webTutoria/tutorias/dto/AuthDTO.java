package com.webTutoria.tutorias.dto;

public class AuthDTO {

    // Lo que envia el frontend al registrarse
    public static class RegistroRequest {
        public String nombre;
        public String email;
        public String password;
    }

    // Lo que envia el frontend al hacer login
    public static class LoginRequest {
        public String email;
        public String password;
    }

    // Lo que envia el frontend al solicitar recuperacion de contrasena
    public static class ResetRequest {
        public String token;
        public String password;
    }

    // Lo que devuelve el servidor tras login/registro exitoso
    public static class AuthResponse {
        public Long id;
        public String nombre;
        public String email;
        public String rol;

        public AuthResponse(Long id, String nombre, String email, String rol) {
            this.id     = id;
            this.nombre = nombre;
            this.email  = email;
            this.rol    = rol;
        }
    }
}