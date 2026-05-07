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

    @PostMapping
    public ResponseEntity<List<Horario>> guardarHorarios(@RequestBody List<Horario> horarios) {
        List<Horario> horariosGuardados = horarioService.guardarTodos(horarios);
        return ResponseEntity.ok(horariosGuardados);
    }

    @GetMapping
    public List<Horario> verHorariosDisponibles(){
        return horarioService.obtenerHorarios();
    }

    @GetMapping("/admin")
    public List<Horario> obtenerHorarios(){
        return horarioService.obtenerHorarios();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHorario(@PathVariable Long id) {
        horarioService.eliminarHorario(id);
        return ResponseEntity.noContent().build();
    }
}
