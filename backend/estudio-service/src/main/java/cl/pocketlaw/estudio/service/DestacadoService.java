package cl.pocketlaw.estudio.service;

import cl.pocketlaw.estudio.model.Destacado;
import cl.pocketlaw.estudio.model.EstudioDtos.DestacadoRequest;
import cl.pocketlaw.estudio.repository.DestacadoRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DestacadoService {

    private final DestacadoRepository repositorio;

    public DestacadoService(DestacadoRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<Destacado> listar(Long usuarioId, String normaId) {
        return repositorio.findByUsuarioIdAndNormaId(usuarioId, normaId);
    }

    public Destacado marcar(DestacadoRequest datos) {
        return repositorio
                .findByUsuarioIdAndNormaIdAndArticulo(datos.usuarioId(), datos.normaId(), datos.articulo())
                .orElseGet(() -> {
                    Destacado nuevo = new Destacado();
                    nuevo.setUsuarioId(datos.usuarioId());
                    nuevo.setNormaId(datos.normaId());
                    nuevo.setArticulo(datos.articulo());
                    return repositorio.save(nuevo);
                });
    }

    public void desmarcar(Long usuarioId, String normaId, int articulo) {
        repositorio.findByUsuarioIdAndNormaIdAndArticulo(usuarioId, normaId, articulo)
                .ifPresent(repositorio::delete);
    }
}
