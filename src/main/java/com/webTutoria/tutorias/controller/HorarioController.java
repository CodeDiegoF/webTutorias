package com.webTutoria.tutorias.controller;
import com.webTutoria.tutorias.model.Horario;
import com.webTutoria.tutorias.service.HorarioService;
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
    public Horario establecerHorarios(@RequestBody Horario horario) {
        return this.horarioService.guardarHorario(horario);
    }

    @GetMapping
    public List<Horario> getHorarios(){
        return horarioService.obtenerHorarios();
    }
}
