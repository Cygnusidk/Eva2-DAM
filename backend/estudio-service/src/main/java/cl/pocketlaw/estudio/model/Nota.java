package cl.pocketlaw.estudio.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** Nota guardada en la tabla "notas" de Xano (snake_case). Hacia la app se devuelve en camelCase. */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Nota {

    private Long id;

    @JsonAlias("usuario_id")
    private Long usuarioId;

    @JsonAlias("norma_id")
    private String normaId;

    private String texto;
    private long fecha;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getNormaId() { return normaId; }
    public void setNormaId(String normaId) { this.normaId = normaId; }
    public String getTexto() { return texto; }
    public void setTexto(String texto) { this.texto = texto; }
    public long getFecha() { return fecha; }
    public void setFecha(long fecha) { this.fecha = fecha; }
}
