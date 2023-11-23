package com.wo.cibertec.services;

import com.wo.cibertec.entities.Reserva;
import com.wo.cibertec.repositories.IReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservaServiceImpl implements IReservaService {
    private final IReservaRepository reservaRepos;

    @Autowired
    public ReservaServiceImpl(IReservaRepository reservaRepos) {
        this.reservaRepos = reservaRepos;
    }

    @Override
    public Reserva save(Reserva reserva) {
        return reservaRepos.save(reserva);
    }
}
