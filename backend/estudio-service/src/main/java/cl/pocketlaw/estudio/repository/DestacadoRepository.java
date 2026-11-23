package cl.pocketlaw.estudio.repository;

import cl.pocketlaw.estudio.model.Destacado;
import cl.pocketlaw.estudio.xano.Xano;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

/** Acceso a la tabla "destacados" de Xano a traves de su API REST. */
@Repository
public class DestacadoRepository {

    private static final ParameterizedTypeReference<List<Destacado>> LISTA = new ParameterizedTypeReference<>() {};

    private final RestClient xano;

    public DestacadoRepository(RestClient xanoHttp) {
        this.xano = xanoHttp;
    }

    // GET /destacados devuelve todos; se filtra por usuario y norma aqui.
    public List<Destacado> findByUsuarioIdAndNormaId(Long usuarioId, String normaId) {
        List<Destacado> todos = Xano.ejecutar(() -> xano.get().uri("/destacados").retrieve().body(LISTA));
        if (todos == null) return List.of();
        return todos.stream()
                .filter(d -> usuarioId.equals(d.getUsuarioId()) && normaId.equals(d.getNormaId()))
                .toList();
    }

    public Optional<Destacado> findByUsuarioIdAndNormaIdAndArticulo(Long usuarioId, String normaId, int articulo) {
        return findByUsuarioIdAndNormaId(usuarioId, normaId).stream()
                .filter(d -> d.getArticulo() == articulo)
                .findFirst();
    }

    public Destacado save(Destacado d) {
        Map<String, Object> cuerpo = Map.of(
                "usuario_id", d.getUsuarioId(),
                "norma_id", d.getNormaId(),
                "articulo", d.getArticulo());
        return Xano.ejecutar(() -> xano.post().uri("/destacados").contentType(MediaType.APPLICATION_JSON)
                .body(cuerpo).retrieve().body(Destacado.class));
    }

    public void delete(Destacado d) {
        Xano.ejecutar(() -> xano.delete().uri("/destacados/{id}", d.getId()).retrieve().toBodilessEntity());
    }
}
