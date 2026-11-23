package cl.pocketlaw.usuarios.repository;

import cl.pocketlaw.usuarios.model.Usuario;
import cl.pocketlaw.usuarios.xano.Xano;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

/** Acceso a la tabla "usuarios" de Xano a traves de su API REST. */
@Repository
public class UsuarioRepository {

    private static final ParameterizedTypeReference<List<Usuario>> LISTA = new ParameterizedTypeReference<>() {};

    private final RestClient xano;

    public UsuarioRepository(RestClient xanoHttp) {
        this.xano = xanoHttp;
    }

    // GET /usuarios devuelve todos los registros; se filtra por correo aqui.
    public Optional<Usuario> findByCorreo(String correo) {
        List<Usuario> todos = Xano.ejecutar(() -> xano.get().uri("/usuarios").retrieve().body(LISTA));
        if (todos == null) return Optional.empty();
        return todos.stream().filter(u -> correo.equalsIgnoreCase(u.getCorreo())).findFirst();
    }

    // GET /usuarios/{id}
    public Optional<Usuario> findById(Long id) {
        return Xano.ejecutar(() -> {
            try {
                return Optional.ofNullable(
                        xano.get().uri("/usuarios/{id}", id).retrieve().body(Usuario.class));
            } catch (HttpClientErrorException.NotFound e) {
                return Optional.empty();
            }
        });
    }

    // POST /usuarios si es nuevo, PATCH /usuarios/{id} si ya existe (se envian todos los campos).
    public Usuario save(Usuario u) {
        Map<String, Object> cuerpo = Map.of(
                "nombre", u.getNombre(),
                "correo", u.getCorreo(),
                "clave_hash", u.getClaveHash(),
                "tamano_letra", u.getTamanoLetra());
        return Xano.ejecutar(() -> {
            if (u.getId() == null) {
                return xano.post().uri("/usuarios").contentType(MediaType.APPLICATION_JSON)
                        .body(cuerpo).retrieve().body(Usuario.class);
            }
            return xano.patch().uri("/usuarios/{id}", u.getId()).contentType(MediaType.APPLICATION_JSON)
                    .body(cuerpo).retrieve().body(Usuario.class);
        });
    }
}
