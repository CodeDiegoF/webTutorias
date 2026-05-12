# Tutorías - Sistema de reservas

Aplicación web con Spring Boot para gestionar horarios de tutorías y reservas de alumnos.

## Funcionalidades principales

- Crear horarios disponibles desde el panel de administración.
- Mostrar al alumno solo los horarios disponibles.
- Reservar una tutoría indicando nombre, email, fecha y hora.
- Cancelar reservas desde la vista del alumno.
- Eliminar horarios desde el panel de administración.

## Nuevos cambios documentados

### 1. Puerto de ejecución

La aplicación arranca por defecto en el puerto `8080`.

En `src/main/resources/application.properties`:

```properties
server.port=8080
```

### 2. Rutas relativas en el frontend

Los archivos `src/main/resources/static/script.js` y `src/main/resources/static/admin.js` ahora usan rutas relativas como:

- `/horarios`
- `/horarios/admin`
- `/reservas`

Esto hace que la aplicación funcione correctamente aunque cambie el puerto.

### 3. Panel de administración

La vista `admin.html` permite:

- Crear horarios nuevos.
- Ver todos los horarios.
- Ver todas las reservas.
- Eliminar horarios.

### 4. Vista de alumno

La vista `index.html` permite:

- Ver horarios disponibles.
- Seleccionar un horario haciendo clic.
- Reservar tutoría.
- Ver y cancelar sus reservas.

### 5. Validación de reservas

El backend valida que una reserva solo se cree si existe un horario para esa fecha y hora.

## Estructura del proyecto

```text
src/main/java/com/webTutoria/tutorias/
├── controller/
├── model/
├── repositorio/
└── service/
```

```text
src/main/resources/static/
├── index.html
├── admin.html
├── script.js
├── admin.js
└── style.css
```

## Ejecutar la aplicación

### Compilar

```powershell
cd "C:\Users\DiegoFA\OneDrive\Escritorio\webTutorias\tutorias\tutorias"
.\mvnw.cmd -DskipTests package
```

### Iniciar

```powershell
cd "C:\Users\DiegoFA\OneDrive\Escritorio\webTutorias\tutorias\tutorias"
.\mvnw.cmd spring-boot:run
```

La aplicación quedará disponible en:

```text
http://localhost:8080
```

## URLs útiles

- Vista del alumno: `http://localhost:8080/index.html`
- Panel de administración: `http://localhost:8080/admin.html`

## Endpoints principales

### Horarios

- `GET /horarios` → devuelve solo horarios disponibles.
- `GET /horarios/admin` → devuelve todos los horarios.
- `POST /horarios` → crea un horario.
- `DELETE /horarios/{id}` → elimina un horario.

### Reservas

- `GET /reservas` → lista todas las reservas.
- `POST /reservas` → crea una reserva si el horario existe y está disponible.
- `DELETE /reservas/{id}` → cancela una reserva.

## Flujo de uso

1. El administrador entra en `admin.html`.
2. Crea horarios disponibles.
3. El alumno entra en `index.html`.
4. El alumno ve solo los horarios disponibles.
5. Selecciona un horario y realiza la reserva.
6. Cuando se reserva, ese horario deja de estar disponible.

## Nota importante

Por ahora el panel de administración está separado en una vista distinta, pero **no hay autenticación real** implementada en el backend. 

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

