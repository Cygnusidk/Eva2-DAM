package cl.pocketlaw.estudio.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public final class EstudioDtos {

    private EstudioDtos() {}

    public record NotaRequest(
            @NotNull Long usuarioId,
            @NotBlank String normaId,
            @NotBlank @Size(min = 5, max = 300) String texto) {}

    public record NotaTextoRequest(@NotBlank @Size(min = 5, max = 300) String texto) {}

    public record DestacadoRequest(
            @NotNull Long usuarioId,
            @NotBlank String normaId,
            @Positive int articulo) {}
}
