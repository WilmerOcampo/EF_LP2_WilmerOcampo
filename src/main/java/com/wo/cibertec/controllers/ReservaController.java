package com.wo.cibertec.controllers;

import com.wo.cibertec.entities.Habitacion;
import com.wo.cibertec.entities.Reserva;
import com.wo.cibertec.entities.Usuario;
import com.wo.cibertec.services.IHabitacionService;
import com.wo.cibertec.services.IReservaService;
import com.wo.cibertec.services.IUsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class ReservaController {
    private final IReservaService reservaService;
    private final IHabitacionService habitacionService;
    private final IUsuarioService usuarioService;

    @Autowired
    public ReservaController(IReservaService reservaService, IHabitacionService habitacionService, IUsuarioService usuarioService) {
        this.reservaService = reservaService;
        this.habitacionService = habitacionService;
        this.usuarioService = usuarioService;
    }

    private List<Optional<Habitacion>> carrito = new ArrayList<Optional<Habitacion>>();

    @GetMapping("/reservas/carrito")
    public String verCarrito(Model model) {
        calcularTotalCarrito(model);
        atributosComunes(model);

        if (!carrito.isEmpty() && carrito.get(0).isPresent()) {
            Optional<Habitacion> firstHabitacion = carrito.get(0);
            if (firstHabitacion.isPresent()) {
                Integer idHab = firstHabitacion.get().getId();
                model.addAttribute("idHab", idHab);
            }
        }
        model.addAttribute("carrito", carrito);
        return "carrito";
    }


    @PostMapping("/reservas/agregarAlCarrito/{id}")
    public ResponseEntity<String> agregarAlCarrito(@PathVariable Integer id) {
        Optional<Habitacion> habitacion = habitacionService.findById(id);
        String mensaje = "Habitación: " + "N°: " + habitacion.get().getNumero() + ", Tipo: " + habitacion.get().getTipo() + ", añadido al carrito.";
        if (habitacion != null) {
            if (carrito.contains(habitacion)) {
                mensaje = "Ya reservaste la habitación: N°: " + habitacion.get().getNumero();
            } else {
                carrito.add(habitacion);
            }
        }
        return ResponseEntity.ok(mensaje);
    }

    @PostMapping("/reservas/quitar/{id}")
    public String quitarHabitacion(@PathVariable Integer id) {
        Optional<Habitacion> habitacionParaQuitar = carrito.stream()
                .filter(p -> p.isPresent() && p.get().getId().equals(id))
                .findFirst()
                .orElse(null);

        if (habitacionParaQuitar != null) {
            carrito.remove(habitacionParaQuitar);
        }
        return "redirect:/reservas/carrito";
    }

    @GetMapping("/reservas/vaciarCarrito")
    public String vaciarCarrito() {
        carrito.clear();
        return "redirect:/";
    }

    @PostMapping("/reservas/save")
    public String saveReserva(@Valid @ModelAttribute("usuario") Usuario usuario,
                              BindingResult uBr,
                              @Valid @ModelAttribute("reserva") Reserva reserva,
                              BindingResult rBr,
                              @RequestParam("idHabitacion") Integer idHabitacion,
                              Model model) {
        if (uBr.hasErrors() || rBr.hasErrors()) {
            return "carrito";
        } else {
            Usuario savedUser = usuarioService.save(usuario);
            reserva.setIdUsuario(savedUser);
            reserva.setEstado("Pendiente");
            Reserva savedReserva = reservaService.save(reserva);
            carrito.clear();
            if (savedReserva != null) {
                LocalDate fechaInicio = reserva.getFecInicio().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate fechaFin = reserva.getFecFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                long diasReserva = ChronoUnit.DAYS.between(fechaInicio, fechaFin);

                double totalPago = diasReserva * savedReserva.getIdHabitacion().getPrecio();

                model.addAttribute("nroHabitacion", savedReserva.getIdHabitacion().getNumero());
                model.addAttribute("idHabitacion", savedReserva.getIdHabitacion().getId());
                model.addAttribute("idUsuario", savedReserva.getIdUsuario().getId());
                model.addAttribute("tipoHabitacion", savedReserva.getIdHabitacion().getTipo());
                model.addAttribute("precioHabitacion", savedReserva.getIdHabitacion().getPrecio());
                model.addAttribute("nombreUsuario", savedUser.getNombre());
                model.addAttribute("id", savedReserva.getId());
                model.addAttribute("montoPago", totalPago);
                model.addAttribute("diasReserva", diasReserva);
                model.addAttribute("fecInicioReserva", reserva.getFecInicio());
                model.addAttribute("fecFinReserva", reserva.getFecFin());
                model.addAttribute("titlePago", "Detalles del Pago");
                atributosComunes(model);

                return "pagar";
            } else {
                model.addAttribute("error", "No se pudo realizar la reserva");
                return "error";
            }
        }
    }

    @PostMapping("/reservas/pagar")
    public String pagarReserva(@ModelAttribute("reserva") Reserva reserva, Model model) {
        try {
            reserva.setEstado("Pagado");
            reservaService.save(reserva);
            atributosComunes(model);
            return "redirect:/";
        } catch (Exception e) {
            return "redirect:/";
        }
    }

    @GetMapping("/reservas/totalCarrito")
    public String calcularTotalCarrito(Model model) {
        double total = 0.0;
        for (Optional<Habitacion> optionalHabitacion : carrito) {
            if (optionalHabitacion.isPresent()) {
                Habitacion habitacion = optionalHabitacion.get();
                total += habitacion.getPrecio() /** habitacion.getCantidad()*/;
            }
        }
        model.addAttribute("totalCarrito", total);
        return "carrito";
    }

    private void atributosComunes(Model model) {
        model.addAttribute("titulo", "Evaluación Final LP2 - WilmerOcampo");
        model.addAttribute("tituloReserva", "Reserva");
        model.addAttribute("tituloReservas", "Reservas");
    }
}
