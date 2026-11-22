package cl.pocketlaw.estudio.controller;

import cl.pocketlaw.estudio.model.Destacado;
import cl.pocketlaw.estudio.model.EstudioDtos.DestacadoRequest;
import cl.pocketlaw.estudio.service.DestacadoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/destacados")
public class DestacadoController {

    private final DestacadoService servicio;

    public DestacadoController(DestacadoService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Destacado> listar(@RequestParam Long usuarioId, @RequestParam String normaId) {
        return servicio.listar(usuarioId, normaId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Destacado marcar(@Valid @RequestBody DestacadoRequest datos) {
        return servicio.marcar(datos);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desmarcar(@RequestParam Long usuarioId, @RequestParam String normaId, @RequestParam int articulo) {
        servicio.desmarcar(usuarioId, normaId, articulo);
    }
}
