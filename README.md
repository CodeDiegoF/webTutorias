# Tutorías - Sistema de Gestión y Reserva

Aplicación web con Spring Boot para gestionar horarios de tutorías, reservas de alumnos e historial de sesiones.

## Estado actual del proyecto

- Autenticación propia por API (`/auth/login`, `/auth/registro`) con almacenamiento en `sessionStorage` del frontend.
- Panel de profesor para crear/eliminar horarios y consultar reservas e historial.
- Vista de alumno para reservar y cancelar tutorías.
- Reglas de negocio activas para evitar reservas inválidas o duplicadas.

## Funcionalidades implementadas

### Profesor (`admin.html`)

- Crear horarios (fecha/hora) marcados como disponibles.
- Ver todos los horarios y filtrarlos por estado (todos/libres/ocupados).
- Eliminar horarios (si había reserva asociada al mismo tramo, se limpia primero).
- Ver reservas futuras.
- Ver historial de tutorías pasadas.

### Alumno (`reservaTutorías.html`)

- Ver solo horarios disponibles y futuros.
- Seleccionar un horario desde la lista.
- Crear una reserva con nombre, email, fecha y hora.
- Ver sus propias reservas filtradas por email.
- Cancelar reservas.

### Login/Registro (`index.html`)

- Registro de alumnos (`rol = ALUMNO`).
- Inicio de sesión con redirección por rol:
  - `PROFESOR` -> `admin.html`
  - `ALUMNO` -> `reservaTutorías.html`

## Reglas de negocio clave

- Una reserva solo se crea si existe un horario para esa fecha/hora.
- No se permite reservar un horario marcado como no disponible.
- No se permiten reservas duplicadas para la misma fecha/hora.
- Al reservar, el horario pasa a `disponible = false`.
- Al cancelar reserva, el horario asociado vuelve a `disponible = true`.

## Arquitectura (backend)

```text
src/main/java/com/webTutoria/tutorias/
|- config/        (SecurityConfig)
|- controller/    (AuthController, HorarioController, ReservaController)
|- dto/           (AuthDTO)
|- model/         (Usuario, Horario, Reserva)
|- repositorio/   (Spring Data JPA repositories)
`- service/       (logica de negocio)
```

## Frontend estático

```text
src/main/resources/static/
|- index.html            (login/registro)
|- login.js
|- admin.html            (panel profesor)
|- admin.js
|- reservaTutorías.html  (panel alumno)
|- reservaScript.js
|- style.css
`- styleLogin.css
```

## Configuración

Archivo: `src/main/resources/application.properties`

Variables soportadas:

- `DB_URL` (default: `jdbc:mysql://localhost:3306/tutoriasbd`)
- `DB_USERNAME` (default: `root`)
- `DB_PASSWORD` (sin default)
- `ADMIN_KEY` (default: `secret-admin`, actualmente informativa)

Propiedades actuales:

```properties
spring.application.name=tutorias
spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/tutoriasbd}
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
app.admin.key=${ADMIN_KEY:secret-admin}
```

## Cómo ejecutar en local (Windows PowerShell)

### 1) Compilar

```powershell
cd "C:\Users\DiegoFA\OneDrive\Escritorio\webTutorias\tutorias\tutorias"
.\mvnw.cmd -DskipTests package
```

### 2) Ejecutar

```powershell
cd "C:\Users\DiegoFA\OneDrive\Escritorio\webTutorias\tutorias\tutorias"
.\mvnw.cmd spring-boot:run
```

## URLs de la aplicación

- Login/Registro: `http://localhost:8080/index.html`
- Panel profesor: `http://localhost:8080/admin.html`
- Panel alumno: `http://localhost:8080/reservaTutorías.html`

## API REST

### Auth

- `POST /auth/registro`
- `POST /auth/login`

### Horarios

- `GET /horarios` -> horarios disponibles (y futuros)
- `GET /horarios/admin` -> todos los horarios
- `POST /horarios` -> crear horario
- `DELETE /horarios/{id}` -> eliminar horario

### Reservas

- `GET /reservas` -> todas las reservas
- `GET /reservas/admin` -> todas las reservas (vista admin)
- `GET /reservas/historial` -> reservas pasadas
- `POST /reservas` -> crear reserva
- `DELETE /reservas/{id}` -> cancelar reserva

## Notas importantes

- El frontend usa rutas relativas en JS (`/horarios`, `/reservas`, etc.), por lo que no depende de un `http://localhost:8080` fijo.
  Si cambias el puerto del servidor, las peticiones seguirán funcionando mientras accedas por la misma base URL de la app.
- `app.admin.key` está definido en propiedades, pero el acceso al panel admin se basa hoy en redirección por rol de usuario en frontend, no en validación de cabecera en los controladores.

## Pruebas

```powershell
cd "C:\Users\DiegoFA\OneDrive\Escritorio\webTutorias\tutorias\tutorias"
.\mvnw.cmd test
```
