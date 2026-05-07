package com.webTutoria.tutorias.repositorio;
import com.webTutoria.tutorias.model.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HorarioRepository extends JpaRepository<Horario, Long> {

    List<Horario> findByDisponibleTrue();

}
