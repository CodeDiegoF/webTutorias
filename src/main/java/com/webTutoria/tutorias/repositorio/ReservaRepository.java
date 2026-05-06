package com.webTutoria.tutorias.repositorio;
import com.webTutoria.tutorias.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.time.LocalTime;

/*Gracias a JpaRepository proporciona métodos CRUD básicos,
  por lo que no sería necesario definirlos aquí.
 */
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    //Con este metodo compruevo si existe una reserva o no
    boolean existsByFechaAndHora(LocalDate fecha, LocalTime hora);
}

