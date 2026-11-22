package cl.pocketlaw.usuarios.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Objetos de entrada y salida de la API. Nunca se devuelve la contraseña. */
public final class UsuarioDtos {

    private UsuarioDtos() {}

    public record RegistroRequest(
            @NotBlank @Size(min = 3, max = 80) String nombre,
            @NotBlank @Email String correo,
            @NotBlank @Size(min = 8, max = 100) String clave) {}

    public record LoginRequest(@NotBlank @Email String correo, @NotBlank String clave) {}

    public record ActualizarRequest(
            @NotBlank @Size(min = 3, max = 80) String nombre,
            @DecimalMin("12") @DecimalMax("32") float tamanoLetra) {}

    public record UsuarioRespuesta(Long id, String nombre, String correo, float tamanoLetra) {
        public static UsuarioRespuesta de(Usuario u) {
            return new UsuarioRespuesta(u.getId(), u.getNombre(), u.getCorreo(), u.getTamanoLetra());
        }
    }
}
