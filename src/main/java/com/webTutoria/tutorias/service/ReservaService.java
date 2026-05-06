package com.webTutoria.tutorias.service;
import com.webTutoria.tutorias.model.Reserva;
import com.webTutoria.tutorias.repositorio.HorarioRepository;
import com.webTutoria.tutorias.repositorio.ReservaRepository;
import org.springframework.stereotype.Service;

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
        
        boolean existe = reservaRepository.existsByFechaAndHora(reserva.getFecha(), reserva.getHora());
        //En caso de que exista lanzo un error y no se guarda la reserva, 
        // si no existe se guarda.
        if (existe) {
            throw new RuntimeException("Ya existe una reserva para esa fecha y hora.");
        }
        horarioRepository.findAll().stream()
                .filter(horario -> horario.getHora().equals(reserva.getHora())
                && horario.getFecha().equals(reserva.getFecha()))
                .findFirst()
                .ifPresent(horario -> {
                    horario.setDisponible(false);
                    horarioRepository.save(horario);
                });
        
        return reservaRepository.save(reserva);
    }

    public List<Reserva> obtenerReservas() {
        return reservaRepository.findAll();
    }
}
