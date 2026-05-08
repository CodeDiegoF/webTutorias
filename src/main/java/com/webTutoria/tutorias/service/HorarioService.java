package com.webTutoria.tutorias.service;
import com.webTutoria.tutorias.model.Horario;
import com.webTutoria.tutorias.model.Reserva;
import com.webTutoria.tutorias.repositorio.HorarioRepository;
import com.webTutoria.tutorias.repositorio.ReservaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class HorarioService {

    private final HorarioRepository horarioRepository;
    private final ReservaRepository reservaRepository;

    public HorarioService(HorarioRepository horarioRepository, ReservaRepository reservaRepository) {
        this.horarioRepository = horarioRepository;
        this.reservaRepository = reservaRepository;
    }

    public Horario guardarHorario(Horario horario) {
        return horarioRepository.save(horario);
    }

    public List<Horario> obtenerHorarios() {
        return horarioRepository.findAll();
    }

    public List<Horario> obtenerHorariosDisponibles() {
        return horarioRepository.findByDisponibleTrue();
    }

    public void eliminarHorario(Long id) {
        Horario horario = horarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Horario no encontrada."));

        reservaRepository.findByFechaAndHora(horario.getFecha(), horario.getHora())
                .ifPresent(reserva -> {
                    reservaRepository.delete(reserva);
                });

        horarioRepository.deleteById(id);
    }

}
