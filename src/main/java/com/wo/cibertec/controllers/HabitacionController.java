package com.wo.cibertec.controllers;

import com.wo.cibertec.entities.Habitacion;
import com.wo.cibertec.services.IHabitacionService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
public class HabitacionController {
    private final IHabitacionService habitacionService;

    @Autowired
    public HabitacionController(IHabitacionService habitacionService) {
        this.habitacionService = habitacionService;
    }

    @GetMapping("")
    public String habitaciones(Model model) {
        List<Habitacion> habitaciones = habitacionService.findAll();
        model.addAttribute("habitaciones", habitaciones);
        atributosComunes(model);
        return "habitaciones";
    }

    @GetMapping("/habitaciones/new")
    public String newhabitacion(Model model) {
        atributosComunes(model);
        model.addAttribute("habitacion", new Habitacion());
        return "habitacion";
    }

    @PostMapping("/habitaciones/save")
    public String saveHabitacion(@Valid Habitacion habitacion, BindingResult br, Model model) throws IOException {
        if (br.hasErrors()) {
            atributosComunes(model);

            return "habitacion";
        }
        habitacionService.save(habitacion);
        return "redirect:/";
    }

    private void atributosComunes(Model model) {
        model.addAttribute("titulo", "Evaluación Final LP2 - WilmerOcampo");
        model.addAttribute("tituloHabitacion", "Habitación");
        model.addAttribute("tituloHabitaciones", "Habitaciones Disponibles");
    }

}
