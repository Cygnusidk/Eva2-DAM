package cl.pocketlaw.estudio.repository;

import cl.pocketlaw.estudio.model.Destacado;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DestacadoRepository extends JpaRepository<Destacado, Long> {
    List<Destacado> findByUsuarioIdAndNormaId(Long usuarioId, String normaId);

    Optional<Destacado> findByUsuarioIdAndNormaIdAndArticulo(Long usuarioId, String normaId, int articulo);
}
