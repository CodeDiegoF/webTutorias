package com.webTutoria.tutorias.controller;
import com.webTutoria.tutorias.model.Reserva;
import com.webTutoria.tutorias.service.ReservaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/reservas")
/**
 * Gestiona la API REST de reservas de tutorias.
 */
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    /**
     * Crea una nueva reserva para un alumno.
     *
     * @param reserva datos de la reserva solicitada
     * @return reserva creada o mensaje de error
     */
    @PostMapping
    public ResponseEntity<?> crearReserva(@RequestBody Reserva reserva) {

        try {

            Reserva nueva = reservaService.guardarReserva(reserva);

            return ResponseEntity.ok(nueva);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    /**
     * Lista todas las reservas existentes.
     *
     * @return lista de reservas
     */
    @GetMapping
    public List<Reserva> obtenerReservas() {
        return reservaService.obtenerReservas();
    }

    /**
     * Endpoint para ver el historial de reservas.
     *
     * @return lista de reservas de los alumnos
     */

    @GetMapping("/historial")
    public List<Reserva> obtenerHistorial() {
        return reservaService.obtenerHistorial();
    }

    /**
     * Endpoint de administracion que devuelve todas las reservas.
     *
     * @return lista completa de reservas
     */
    @GetMapping("/admin")
    public List<Reserva> obtenerReservasAdmin() {
        return reservaService.obtenerReservas();
    }

    /**
     * Elimina una reserva y libera su horario asociado si existe.
     *
     * @param id identificador de la reserva
     * @return respuesta HTTP 204 sin contenido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReserva(@PathVariable Long id) {
        reservaService.eliminarReserva(id);
        return ResponseEntity.noContent().build();
    }
}