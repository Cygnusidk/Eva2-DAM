package cl.pocketlaw.usuarios.xano;

import java.util.function.Supplier;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

/** Ejecuta una llamada a Xano y traduce sus fallos a 502 (Bad Gateway) para la app. */
public final class Xano {

    private Xano() {}

    public static <T> T ejecutar(Supplier<T> llamada) {
        try {
            return llamada.get();
        } catch (RestClientResponseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Xano respondio con error " + e.getStatusCode().value());
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "No se pudo conectar con Xano");
        }
    }
}
