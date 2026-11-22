package cl.pocketlaw.usuarios.controller;

import cl.pocketlaw.usuarios.model.UsuarioDtos.ActualizarRequest;
import cl.pocketlaw.usuarios.model.UsuarioDtos.LoginRequest;
import cl.pocketlaw.usuarios.model.UsuarioDtos.RegistroRequest;
import cl.pocketlaw.usuarios.model.UsuarioDtos.UsuarioRespuesta;
import cl.pocketlaw.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService servicio;

    public UsuarioController(UsuarioService servicio) {
        this.servicio = servicio;
    }

    // POST /api/usuarios/registro
    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioRespuesta registrar(@Valid @RequestBody RegistroRequest datos) {
        return servicio.registrar(datos);
    }

    // POST /api/usuarios/login
    @PostMapping("/login")
    public UsuarioRespuesta iniciarSesion(@Valid @RequestBody LoginRequest datos) {
        return servicio.iniciarSesion(datos);
    }

    // GET /api/usuarios/{id}
    @GetMapping("/{id}")
    public UsuarioRespuesta obtener(@PathVariable Long id) {
        return servicio.obtener(id);
    }

    // PUT /api/usuarios/{id}
    @PutMapping("/{id}")
    public UsuarioRespuesta actualizar(@PathVariable Long id, @Valid @RequestBody ActualizarRequest datos) {
        return servicio.actualizar(id, datos);
    }
}
