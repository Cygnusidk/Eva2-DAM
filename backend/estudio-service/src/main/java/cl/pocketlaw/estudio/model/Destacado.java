package cl.pocketlaw.estudio.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "destacados",
        uniqueConstraints = @UniqueConstraint(columnNames = {"usuarioId", "normaId", "articulo"}))
public class Destacado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long usuarioId;

    @Column(nullable = false)
    private String normaId;

    @Column(nullable = false)
    private int articulo;

    public Long getId() { return id; }
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    public String getNormaId() { return normaId; }
    public void setNormaId(String normaId) { this.normaId = normaId; }
    public int getArticulo() { return articulo; }
    public void setArticulo(int articulo) { this.articulo = articulo; }
}
