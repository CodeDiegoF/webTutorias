package com.webTutoria.tutorias.repositorio;
import com.webTutoria.tutorias.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link Reserva}.
 * JpaRepository ya aporta operaciones CRUD basicas.
 */
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    /**
     * Comprueba si ya existe una reserva para la fecha y hora indicadas.
     */
    boolean existsByFechaAndHora(LocalDate fecha, LocalTime hora);

    /**
     * Recupera la reserva para una fecha/hora exacta, si existe.
     */
    Optional<Reserva> findByFechaAndHora(LocalDate fecha, LocalTime hora);
}

