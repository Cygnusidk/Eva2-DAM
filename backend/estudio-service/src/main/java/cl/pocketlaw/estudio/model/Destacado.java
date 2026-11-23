package cl.pocketlaw.estudio.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/** Articulo destacado, guardado en la tabla "destacados" de Xano. */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Destacado {

    private Long id;

    @JsonAlias("usuario_id")
    private Long usuarioId;

    @JsonAlias("norma_id")
    private String normaId;

    private int articulo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getNormaId() { return normaId; }
    public void setNormaId(String normaId) { this.normaId = normaId; }
    public int getArticulo() { return articulo; }
    public void setArticulo(int articulo) { this.articulo = articulo; }
}
