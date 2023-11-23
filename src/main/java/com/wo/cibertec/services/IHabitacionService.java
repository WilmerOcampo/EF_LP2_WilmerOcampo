package com.wo.cibertec.services;

import com.wo.cibertec.entities.Habitacion;

import java.util.List;
import java.util.Optional;

public interface IHabitacionService {
    List<Habitacion> findAll();

    Habitacion save(Habitacion habitacion);

    Optional<Habitacion> findById(Integer id);
}
