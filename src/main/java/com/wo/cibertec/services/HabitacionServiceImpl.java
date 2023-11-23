package com.wo.cibertec.services;

import com.wo.cibertec.entities.Habitacion;
import com.wo.cibertec.repositories.IHabitacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HabitacionServiceImpl implements IHabitacionService {
    private final IHabitacionRepository habitacionRepos;

    @Autowired
    public HabitacionServiceImpl(IHabitacionRepository habitacionRepos) {
        this.habitacionRepos = habitacionRepos;
    }

    @Override
    public List<Habitacion> findAll() {
        return habitacionRepos.findAll();
    }

    @Override
    public Habitacion save(Habitacion habitacion) {
        return habitacionRepos.save(habitacion);
    }

    @Override
    public Optional<Habitacion> findById(Integer id) {
        return habitacionRepos.findById(id);
    }
}
