package com.webTutoria.tutorias.service;
import com.webTutoria.tutorias.model.Horario;
import com.webTutoria.tutorias.repositorio.HorarioRepository;
import com.webTutoria.tutorias.repositorio.ReservaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
/**
 * Contiene la logica de negocio asociada a la gestion de horarios de tutoria.
 */
public class HorarioService {

    private final HorarioRepository horarioRepository;
    private final ReservaRepository reservaRepository;

    public HorarioService(HorarioRepository horarioRepository, ReservaRepository reservaRepository) {
        this.horarioRepository = horarioRepository;
        this.reservaRepository = reservaRepository;
    }

    /**
     * Guarda un nuevo horario publicado por el profesor.
     */
    public Horario guardarHorario(Horario horario) {
        return horarioRepository.save(horario);
    }

    /**
     * Obtiene todos los horarios, incluidos ocupados y disponibles.
     */
    public List<Horario> obtenerHorarios() {
        return horarioRepository.findAll();
    }

    /**
     * Obtiene unicamente los horarios disponibles para alumnos
     * a partir de la fecha y hora actual.
     */
    public List<Horario> obtenerDisponibles() {
        LocalDate fechaHoy = LocalDate.now();
        LocalTime horaActual = LocalTime.now();

        return horarioRepository.findByDisponibleTrue().stream()
                .filter(h -> h.getFecha().isAfter(fechaHoy)  ||
                        (h.getFecha().isEqual(fechaHoy) && h.getHora().isAfter(horaActual)))
                .collect(java.util.stream.Collectors.toList());

    }

    /**
     * Elimina un horario por id. Si existe una reserva en la misma fecha/hora,
     * primero elimina la reserva para mantener la consistencia.
     */
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

    /**
     * Busca horarios pasados (fecha/hora anteriores al momento actual) para mostrar el historial.
     */
    public List<Horario> obtenerHistorialHorarios() {
        LocalDate hoy = LocalDate.now();
        LocalTime ahora = LocalTime.now();

        return horarioRepository.findAll().stream()
                .filter(h -> h.getFecha().isBefore(hoy) ||
                        (h.getFecha().isEqual(hoy) && h.getHora().isBefore(ahora)))
                .collect(java.util.stream.Collectors.toList());
    }
}
