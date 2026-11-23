package cl.pocketlaw.usuarios.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Usuario registrado en PocketLaw, tal como se guarda en la tabla "usuarios" de Xano.
 * La contrasena se guarda solo como hash BCrypt. Xano usa snake_case (clave_hash, tamano_letra).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Usuario {

    private Long id;
    private String nombre;
    private String correo;

    @JsonAlias("clave_hash")
    private String claveHash;

    @JsonAlias("tamano_letra")
    private float tamanoLetra = 16f;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getClaveHash() { return claveHash; }
    public void setClaveHash(String claveHash) { this.claveHash = claveHash; }
    public float getTamanoLetra() { return tamanoLetra; }
    public void setTamanoLetra(float tamanoLetra) { this.tamanoLetra = tamanoLetra; }
}
