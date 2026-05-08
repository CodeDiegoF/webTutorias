package com.webTutoria.tutorias.controller;
import com.webTutoria.tutorias.model.Reserva;
import com.webTutoria.tutorias.service.ReservaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

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

    @GetMapping
    public List<Reserva> obtenerReservas() {
        return reservaService.obtenerReservas();
    }

    @GetMapping("/admin")
    public List<Reserva> obtenerReservasAdmin() {
        return reservaService.obtenerReservas();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReserva(@PathVariable Long id) {
        reservaService.eliminarReserva(id);
        return ResponseEntity.noContent().build();
    }
}