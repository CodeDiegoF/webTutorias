package com.webTutoria.tutorias.service;
import com.webTutoria.tutorias.model.Reserva;
import com.webTutoria.tutorias.repositorio.HorarioRepository;
import com.webTutoria.tutorias.repositorio.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final HorarioRepository horarioRepository;

    public ReservaService(ReservaRepository reservaRepository, HorarioRepository horarioRepository) {
        this.reservaRepository = reservaRepository;
        this.horarioRepository = horarioRepository;
    }

    public Reserva guardarReserva(Reserva reserva) {
        // Comprobar que existe un horario disponible para la fecha/hora indicada
        var horarioOpt = horarioRepository.findByFechaAndHora(reserva.getFecha(), reserva.getHora());
        if (horarioOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No existe un horario para esa fecha y hora.");
        }

        var horario = horarioOpt.get();
        if (!horario.isDisponible()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El horario no está disponible.");
        }

        // Evitar duplicados de reserva
        boolean existe = reservaRepository.existsByFechaAndHora(reserva.getFecha(), reserva.getHora());
        if (existe) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe una reserva para esa fecha y hora.");
        }

        // Marcar horario como no disponible y guardar la reserva
        horario.setDisponible(false);
        horarioRepository.save(horario);

        return reservaRepository.save(reserva);
    }

    public List<Reserva> obtenerReservas() {
        return reservaRepository.findAll();
    }

    public void eliminarReserva(Long id) {
        reservaRepository.deleteById(id);
    }
}
