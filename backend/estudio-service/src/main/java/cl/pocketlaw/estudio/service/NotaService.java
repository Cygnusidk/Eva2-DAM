package cl.pocketlaw.estudio.service;

import cl.pocketlaw.estudio.model.EstudioDtos.NotaRequest;
import cl.pocketlaw.estudio.model.EstudioDtos.NotaTextoRequest;
import cl.pocketlaw.estudio.model.Nota;
import cl.pocketlaw.estudio.repository.NotaRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class NotaService {

    private final NotaRepository repositorio;

    public NotaService(NotaRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<Nota> listar(Long usuarioId) {
        return repositorio.findByUsuarioIdOrderByFechaDesc(usuarioId);
    }

    public Nota crear(NotaRequest datos) {
        Nota nota = new Nota();
        nota.setUsuarioId(datos.usuarioId());
        nota.setNormaId(datos.normaId());
        nota.setTexto(datos.texto().trim());
        nota.setFecha(System.currentTimeMillis());
        return repositorio.save(nota);
    }

    public Nota editar(Long id, NotaTextoRequest datos) {
        Nota nota = repositorio.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nota no encontrada"));
        nota.setTexto(datos.texto().trim());
        nota.setFecha(System.currentTimeMillis());
        return repositorio.save(nota);
    }

    public void eliminar(Long id) {
        if (repositorio.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nota no encontrada");
        }
        repositorio.deleteById(id);
    }
}
