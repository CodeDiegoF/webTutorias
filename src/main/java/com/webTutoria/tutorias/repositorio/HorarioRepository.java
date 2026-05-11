package com.webTutoria.tutorias.repositorio;
import com.webTutoria.tutorias.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para operaciones de persistencia de {@link Horario}.
 */
public interface HorarioRepository extends JpaRepository<Horario, Long> {

    /**
     * Recupera los horarios libres para que el alumno solo vea opciones reservables.
     */
    List<Horario> findByDisponibleTrue();

    /**
     * Localiza un horario exacto por fecha y hora, independientemente de su estado.
     */
    Optional<Horario> findByFechaAndHora(LocalDate fecha, LocalTime hora);

}
