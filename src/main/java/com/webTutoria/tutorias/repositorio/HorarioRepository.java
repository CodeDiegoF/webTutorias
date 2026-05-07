package com.webTutoria.tutorias.repositorio;
import com.webTutoria.tutorias.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface HorarioRepository extends JpaRepository<Horario, Long> {

    List<Horario> findByDisponibleTrue();

    boolean existsByFechaAndHoraAndDisponibleTrue(LocalDate fecha, LocalTime hora);

    Optional<Horario> findByFechaAndHora(LocalDate fecha, LocalTime hora);

}
