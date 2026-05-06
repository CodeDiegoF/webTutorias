package com.webTutoria.tutorias.repositorio;
import com.webTutoria.tutorias.model.Horario;
import com.webTutoria.tutorias.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface HorarioRepository extends JpaRepository<Horario, Long> {

    List<Horario> findByDisponibleTrue();

}
