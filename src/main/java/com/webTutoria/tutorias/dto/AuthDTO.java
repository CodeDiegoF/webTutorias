package com.webTutoria.tutorias.dto;

public class AuthDTO {

    // Lo que envía el frontend al registrarse
    public static class RegistroRequest {
        public String nombre;
        public String email;
        public String password;
    }

    // Lo que envía el frontend al hacer login
    public static class LoginRequest {
        public String email;
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