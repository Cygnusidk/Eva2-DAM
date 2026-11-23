package cl.pocketlaw.estudio.repository;

import cl.pocketlaw.estudio.model.Nota;
import cl.pocketlaw.estudio.xano.Xano;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

/** Acceso a la tabla "notas" de Xano a traves de su API REST. */
@Repository
public class NotaRepository {

    private static final ParameterizedTypeReference<List<Nota>> LISTA = new ParameterizedTypeReference<>() {};

    private final RestClient xano;

    public NotaRepository(RestClient xanoHttp) {
        this.xano = xanoHttp;
    }

    // GET /notas devuelve todas; se filtra por usuario y se ordena por fecha descendente.
    public List<Nota> findByUsuarioIdOrderByFechaDesc(Long usuarioId) {
        List<Nota> todas = Xano.ejecutar(() -> xano.get().uri("/notas").retrieve().body(LISTA));
        if (todas == null) return List.of();
        return todas.stream()
                .filter(n -> usuarioId.equals(n.getUsuarioId()))
                .sorted(Comparator.comparingLong(Nota::getFecha).reversed())
                .toList();
    }

    public Optional<Nota> findById(Long id) {
        return Xano.ejecutar(() -> {
            try {
                return Optional.ofNullable(xano.get().uri("/notas/{id}", id).retrieve().body(Nota.class));
            } catch (HttpClientErrorException.NotFound e) {
                return Optional.empty();
            }
        });
    }

    // POST /notas si es nueva, PATCH /notas/{id} si ya existe.
    public Nota save(Nota n) {
        Map<String, Object> cuerpo = Map.of(
                "usuario_id", n.getUsuarioId(),
                "norma_id", n.getNormaId(),
                "texto", n.getTexto(),
                "fecha", n.getFecha());
        return Xano.ejecutar(() -> {
            if (n.getId() == null) {
                return xano.post().uri("/notas").contentType(MediaType.APPLICATION_JSON)
                        .body(cuerpo).retrieve().body(Nota.class);
            }
            return xano.patch().uri("/notas/{id}", n.getId()).contentType(MediaType.APPLICATION_JSON)
                    .body(cuerpo).retrieve().body(Nota.class);
        });
    }

    public void deleteById(Long id) {
        Xano.ejecutar(() -> xano.delete().uri("/notas/{id}", id).retrieve().toBodilessEntity());
    }
}
