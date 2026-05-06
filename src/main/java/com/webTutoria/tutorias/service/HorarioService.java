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

    public Horario guardarHorario(Horario horario) {
        return horarioRepository.save(horario);
    }

    public List<Horario> obtenerHorarios() {
        return horarioRepository.findAll();
    }

}
