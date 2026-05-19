package com.webTutoria.tutorias.service;
import com.webTutoria.tutorias.model.Reserva;
import com.webTutoria.tutorias.repositorio.HorarioRepository;
import com.webTutoria.tutorias.repositorio.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
/**
 * Gestiona la logica de negocio de reservas y su sincronizacion con horarios.
 */
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final HorarioRepository horarioRepository;

    public ReservaService(ReservaRepository reservaRepository, HorarioRepository horarioRepository) {
        this.reservaRepository = reservaRepository;
        this.horarioRepository = horarioRepository;
    }

    /**
     * Crea una reserva validando existencia de horario, disponibilidad y duplicados.
     * Al reservar, el horario pasa a estado no disponible.
     */
    public Reserva guardarReserva(Reserva reserva) {
        // Verifica que la fecha/hora solicitada exista en la oferta de horarios.
        var horarioOpt = horarioRepository.findByFechaAndHora(reserva.getFecha(), reserva.getHora());
        if (horarioOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No existe un horario para esa fecha y hora.");
        }

        var horario = horarioOpt.get();
        if (!horario.isDisponible()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El horario no está disponible.");
        }

        // Evita dos reservas en el mismo tramo temporal.
        boolean existe = reservaRepository.existsByFechaAndHora(reserva.getFecha(), reserva.getHora());
        if (existe) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe una reserva para esa fecha y hora.");
        }

        // Reserva aceptada: marca el horario como ocupado y persiste la reserva.
        horario.setDisponible(false);
        horarioRepository.save(horario);

        return reservaRepository.save(reserva);
    }

    /**
     * Recupera todas las reservas almacenadas.
     */
    public List<Reserva> obtenerReservas() {
        return reservaRepository.findAll();
    }

    /**
     * Elimina una reserva y reactiva su horario asociado, si existe.
     */
    public void eliminarReserva(Long id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Reserva no encontrada."));

        horarioRepository.findByFechaAndHora(reserva.getFecha(), reserva.getHora())
                .ifPresent(horario -> {
                    horario.setDisponible(true);
                    horarioRepository.save(horario);
                });

        reservaRepository.deleteById(id);
    }

    /**
     * Busca reservas pasadas (fecha/hora anteriores al momento actual) para mostrar el historial.
     */
    public List<Reserva> obtenerHistorial() {
        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();

        return reservaRepository.findAll().stream()
                .filter(r -> r.getFecha().isBefore(hoy) ||
                        (r.getFecha().isEqual(hoy) && r.getHora().isBefore(ahora)))
                .collect(java.util.stream.Collectors.toList());
    }
}
