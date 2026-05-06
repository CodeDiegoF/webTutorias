package com.webTutoria.tutorias.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

// Añado un restricción que permita que no se puedan crear reservas con la misma fecha y hora.
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"fecha", "hora"})
})


public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Le dice a la BD que genere automáticamente el valor al insertar un nuevo registro.
    private Long id;

    private String nombreAlumno;
    private LocalDate fecha;
    private LocalTime hora;

}