# PocketLaw - Microservicios (Spring Boot)

Requisitos: JDK 17 y Maven (o el Maven integrado de IntelliJ / STS).

| Servicio | Puerto | Carpeta | Base de datos |
|---|---|---|---|
| Usuarios | 8081 | `usuarios-service` | H2 en archivo `./data/usuarios` |
| Estudio (notas y destacados) | 8082 | `estudio-service` | H2 en archivo `./data/estudio` |

## Ejecutar
```
cd usuarios-service && mvn spring-boot:run
cd estudio-service  && mvn spring-boot:run     (en otra terminal)
```
Consola de la base: http://localhost:8081/h2-console y http://localhost:8082/h2-console
(JDBC URL `jdbc:h2:file:./data/usuarios` o `./data/estudio`, usuario `sa`, sin contraseña).

## Endpoints
| Método | Ruta | Descripción | Respuestas |
|---|---|---|---|
| POST | `:8081/api/usuarios/registro` | Crear cuenta | 201 / 400 / 409 |
| POST | `:8081/api/usuarios/login` | Iniciar sesión | 200 / 401 |
| GET | `:8081/api/usuarios/{id}` | Obtener usuario | 200 / 404 |
| PUT | `:8081/api/usuarios/{id}` | Actualizar nombre y tamaño de letra | 200 / 404 |
| GET | `:8082/api/notas?usuarioId=` | Listar notas | 200 |
| POST | `:8082/api/notas` | Crear nota | 201 / 400 |
| PUT | `:8082/api/notas/{id}` | Editar nota | 200 / 404 |
| DELETE | `:8082/api/notas/{id}` | Eliminar nota | 204 / 404 |
| GET | `:8082/api/destacados?usuarioId=&normaId=` | Listar destacados | 200 |
| POST | `:8082/api/destacados` | Marcar artículo | 201 |
| DELETE | `:8082/api/destacados?usuarioId=&normaId=&articulo=` | Desmarcar | 204 |

## Prueba rápida
```
curl -X POST localhost:8081/api/usuarios/registro -H "Content-Type: application/json" \
  -d '{"nombre":"Prueba Uno","correo":"prueba@pocketlaw.cl","clave":"Prueba123"}'
curl -X POST localhost:8082/api/notas -H "Content-Type: application/json" \
  -d '{"usuarioId":1,"normaId":"codigo_civil","texto":"Repasar el artículo 1"}'
```
