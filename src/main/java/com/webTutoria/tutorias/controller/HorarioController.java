package com.webTutoria.tutorias.controller;
import com.webTutoria.tutorias.model.Horario;
import com.webTutoria.tutorias.service.HorarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/horarios")
public class HorarioController {

    private final HorarioService horarioService;

    public HorarioController(HorarioService horarioService) {
        this.horarioService = horarioService;
    }

    /**
     * Crea un horario nuevo.
     *
     * @param horario horario que se desea persistir
     * @return horario guardado o mensaje de error
     */
    @PostMapping
    public ResponseEntity<?> guardarHorarios(@RequestBody Horario horario) {
        try {

            Horario horarioGuardado = horarioService.guardarHorario(horario);
            return ResponseEntity.ok(horarioGuardado);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    /**
     * Devuelve solo los horarios disponibles para la reserva por parte del alumno.
     *
     * @return lista de horarios marcados como disponibles
     */
    @GetMapping
    public List<Horario> verHorariosDisponibles(){
        return horarioService.obtenerHorariosDisponibles();
    }

    /**
     * Devuelve todos los horarios para la vista de administracion.
     *
     * @return lista completa de horarios
     */
    @GetMapping("/admin")
    public List<Horario> obtenerHorarios(){
        return horarioService.obtenerHorarios();
    }

    /**
     * Elimina un horario por id.
     * Si existe una reserva enlazada al mismo tramo fecha/hora, se limpia primero.
     *
     * @param id identificador del horario
     * @return respuesta HTTP 204 sin contenido
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHorario(@PathVariable Long id) {
        horarioService.eliminarHorario(id);
        return ResponseEntity.noContent().build();
    }
}
