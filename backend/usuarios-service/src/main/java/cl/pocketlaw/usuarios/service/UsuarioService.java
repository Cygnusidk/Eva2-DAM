package cl.pocketlaw.usuarios.service;

import cl.pocketlaw.usuarios.model.Usuario;
import cl.pocketlaw.usuarios.model.UsuarioDtos.ActualizarRequest;
import cl.pocketlaw.usuarios.model.UsuarioDtos.LoginRequest;
import cl.pocketlaw.usuarios.model.UsuarioDtos.RegistroRequest;
import cl.pocketlaw.usuarios.model.UsuarioDtos.UsuarioRespuesta;
import cl.pocketlaw.usuarios.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {

    private final UsuarioRepository repositorio;
    private final BCryptPasswordEncoder cifrador = new BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository repositorio) {
        this.repositorio = repositorio;
    }

    // Crea la cuenta. Lanza 409 si el correo ya existe.
    public UsuarioRespuesta registrar(RegistroRequest datos) {
        String correo = datos.correo().trim().toLowerCase();
        if (repositorio.findByCorreo(correo).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(datos.nombre().trim());
        usuario.setCorreo(correo);
        usuario.setClaveHash(cifrador.encode(datos.clave()));
        return UsuarioRespuesta.de(repositorio.save(usuario));
    }

    // Valida el correo y la clave. Lanza 401 si no coinciden.
    public UsuarioRespuesta iniciarSesion(LoginRequest datos) {
        Usuario usuario = repositorio.findByCorreo(datos.correo().trim().toLowerCase())
                .filter(u -> cifrador.matches(datos.clave(), u.getClaveHash()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Correo o contraseña incorrectos"));
        return UsuarioRespuesta.de(usuario);
    }

    public UsuarioRespuesta obtener(Long id) {
        return UsuarioRespuesta.de(buscar(id));
    }

    // Actualiza el nombre y el tamaño de letra del usuario.
    public UsuarioRespuesta actualizar(Long id, ActualizarRequest datos) {
        Usuario usuario = buscar(id);
        usuario.setNombre(datos.nombre().trim());
        usuario.setTamanoLetra(datos.tamanoLetra());
        return UsuarioRespuesta.de(repositorio.save(usuario));
    }

    private Usuario buscar(Long id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }
}
