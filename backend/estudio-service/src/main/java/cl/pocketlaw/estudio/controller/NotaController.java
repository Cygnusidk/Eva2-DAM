package cl.pocketlaw.estudio.controller;

import cl.pocketlaw.estudio.model.EstudioDtos.NotaRequest;
import cl.pocketlaw.estudio.model.EstudioDtos.NotaTextoRequest;
import cl.pocketlaw.estudio.model.Nota;
import cl.pocketlaw.estudio.service.NotaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notas")
public class NotaController {

    private final NotaService servicio;

    public NotaController(NotaService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public List<Nota> listar(@RequestParam Long usuarioId) {
        return servicio.listar(usuarioId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Nota crear(@Valid @RequestBody NotaRequest datos) {
        return servicio.crear(datos);
    }

    @PutMapping("/{id}")
    public Nota editar(@PathVariable Long id, @Valid @RequestBody NotaTextoRequest datos) {
        return servicio.editar(id, datos);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        servicio.eliminar(id);
    }
}
