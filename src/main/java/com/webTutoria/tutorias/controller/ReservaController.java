package com.webTutoria.tutorias.controller;
import com.webTutoria.tutorias.model.Reserva;
import com.webTutoria.tutorias.service.ReservaService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService reserva;

    public ReservaController(ReservaService reservaService) {
        this.reserva = reservaService;
    }

    @PostMapping
    public Reserva crearReserva(@RequestBody Reserva reserva) {
        return this.reserva.guardarReserva(reserva);
    }

    @GetMapping
    public List<Reserva> obtenerReservas() {
        return reserva.obtenerReservas();
    }
}