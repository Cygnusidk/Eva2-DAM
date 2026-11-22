package cl.pocketlaw.estudio.repository;

import cl.pocketlaw.estudio.model.Nota;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotaRepository extends JpaRepository<Nota, Long> {
    List<Nota> findByUsuarioIdOrderByFechaDesc(Long usuarioId);
}
