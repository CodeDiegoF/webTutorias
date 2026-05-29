package com.webTutoria.tutorias.service;
import com.webTutoria.tutorias.model.Horario;
import com.webTutoria.tutorias.repositorio.HorarioRepository;
import com.webTutoria.tutorias.repositorio.ReservaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
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
    private final EmailService emailService;

    public HorarioService(HorarioRepository horarioRepository,
                          ReservaRepository reservaRepository,
                          EmailService emailService) {
        this.horarioRepository = horarioRepository;
        this.reservaRepository = reservaRepository;
        this.emailService = emailService;
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
    @Transactional
    public void eliminarHorario(Long id) {
        Horario horario = horarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Horario no encontrado."));

        reservaRepository.findByFechaAndHora(horario.getFecha(), horario.getHora())
                .stream().findFirst()
                .ifPresent(reserva -> {
                    // Capturamos variables locales
                    String emailAlumno = reserva.getEmailAlumno();
                    String nombreAlumno = reserva.getNombreAlumno();
                    String fecha = horario.getFecha().toString();
                    String hora = horario.getHora().toString();

                    // Borramos la reserva asociada
                    reservaRepository.delete(reserva);

                    //  Registramos el envío del email para después del commit
                    TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            emailService.notificarAlumnoHorarioEliminado(emailAlumno, nombreAlumno, fecha, hora);
                        }
                    });
                });

        // Borramos el horario
        horarioRepository.delete(horario);
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
