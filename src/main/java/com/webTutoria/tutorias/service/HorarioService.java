package com.webTutoria.tutorias.service;
import com.webTutoria.tutorias.model.Horario;
import com.webTutoria.tutorias.repositorio.HorarioRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HorarioService {

    private final HorarioRepository horarioRepository;

    public HorarioService(HorarioRepository horarioRepository) {
        this.horarioRepository = horarioRepository;
    }

    public List<Horario> guardarTodos(List<Horario> horarios) {
        return horarioRepository.saveAll(horarios);
    }

    public List<Horario> obtenerHorarios() {
        return horarioRepository.findAll();
    }

    public void eliminarHorario(Long id) {
        horarioRepository.deleteById(id);
    }

}
