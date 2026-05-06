package com.webTutoria.tutorias.service;
import com.webTutoria.tutorias.model.Reserva;
import com.webTutoria.tutorias.repositorio.ReservaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    public Reserva guardarReserva(Reserva reserva) {
        
        boolean existe = reservaRepository.existsByFechaAndHora(reserva.getFecha(), reserva.getHora());
        //En caso de que exista lanzo un error y no se guarda la reserva, 
        // si no existe se guarda.
        if (existe) {
            throw new RuntimeException("Ya existe una reserva para esa fecha y hora.");
        }
        
        return reservaRepository.save(reserva);
    }

    public List<Reserva> obtenerReservas() {
        return reservaRepository.findAll();
    }
}
