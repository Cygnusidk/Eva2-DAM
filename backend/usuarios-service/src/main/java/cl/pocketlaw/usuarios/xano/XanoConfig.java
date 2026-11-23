package cl.pocketlaw.usuarios.xano;

import java.net.http.HttpClient;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

/** Cliente HTTP hacia el API Group de Xano. La URL base viene de la configuracion externa. */
@Configuration
public class XanoConfig {

    @Bean
    public RestClient xanoHttp(@Value("${xano.base-url}") String urlBase) {
        // El cliente HTTP de Java 11+ soporta PATCH (el de HttpURLConnection no).
        HttpClient cliente = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        JdkClientHttpRequestFactory fabrica = new JdkClientHttpRequestFactory(cliente);
        fabrica.setReadTimeout(Duration.ofSeconds(10));
        return RestClient.builder()
                .baseUrl(urlBase.trim().replaceAll("/+$", ""))
                .requestFactory(fabrica)
                .build();
    }
}
