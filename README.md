# Sistema de Tutorias

Aplicacion web con Spring Boot para gestionar horarios de tutoria y reservas de alumnos.

## Estructura del proyecto

- `src/main/java/com/webTutoria/tutorias/model`: entidades JPA (`Horario`, `Reserva`).
- `src/main/java/com/webTutoria/tutorias/repositorio`: acceso a datos con Spring Data JPA.
- `src/main/java/com/webTutoria/tutorias/service`: logica de negocio.
- `src/main/java/com/webTutoria/tutorias/controller`: API REST.
- `src/main/resources/static`: interfaz web (alumno y admin).

## Flujo funcional

1. El profesor crea horarios desde `admin.html`.
2. El alumno visualiza horarios disponibles en `index.html`.
3. El alumno reserva un horario.
4. El horario reservado se marca como no disponible.
5. Al eliminar una reserva, el horario vuelve a disponible.

## Endpoints principales

### Horarios

- `GET /horarios`: devuelve solo horarios disponibles.
- `GET /horarios/admin`: devuelve todos los horarios.
- `POST /horarios`: crea un horario.
- `DELETE /horarios/{id}`: elimina un horario.

### Reservas

- `GET /reservas`: devuelve todas las reservas.
- `GET /reservas/admin`: devuelve todas las reservas (vista admin).
- `POST /reservas`: crea una reserva si el horario existe y esta disponible.
- `DELETE /reservas/{id}`: elimina reserva y libera su horario.

## Validaciones de negocio

- No se permite reservar una fecha/hora sin horario creado previamente.
- No se permite reservar un horario marcado como no disponible.
- No se permiten reservas duplicadas para la misma fecha/hora.

## Configuracion local

Archivo: `src/main/resources/application.properties`

- Base de datos: H2 en memoria.
- Esquema JPA: `create-drop`.
- SQL visible en consola: activado.
- Clave admin simple: `app.admin.key`.

## Ejecutar en local

```powershell
cd "C:\Users\DiegoFA\OneDrive\Escritorio\webTutorias\tutorias\tutorias"
.\mvnw.cmd spring-boot:run
```

## Ejecutar pruebas

```powershell
cd "C:\Users\DiegoFA\OneDrive\Escritorio\webTutorias\tutorias\tutorias"
.\mvnw.cmd test
```

## Vistas web

- Alumno: `http://localhost:8080/index.html`
- Administrador: `http://localhost:8080/admin.html`

