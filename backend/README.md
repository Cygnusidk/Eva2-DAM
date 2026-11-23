# PocketLaw - Microservicios (Spring Boot + Xano)

Requisitos: JDK 17 y Maven (o el Maven integrado de IntelliJ / STS).

Arquitectura: `App Android -> Microservicios Spring Boot -> Xano (base de datos en la nube)`.
Los microservicios mantienen la logica (validaciones, cifrado BCrypt de contrasenas, correo unico,
destacados sin duplicar) y guardan los datos en tablas de Xano mediante su API REST.

| Servicio | Puerto | Carpeta | Tablas en Xano |
|---|---|---|---|
| Usuarios | 8081 | `usuarios-service` | `usuarios` |
| Estudio (notas y destacados) | 8082 | `estudio-service` | `notas`, `destacados` |

## Configurar Xano
1. En Xano crea las tablas (todas con `id` autoincremental) dentro de un API Group con endpoints CRUD publicos:
   - `usuarios`: `nombre` (text), `correo` (text), `clave_hash` (text), `tamano_letra` (decimal, default 16)
   - `notas`: `usuario_id` (integer), `norma_id` (text), `texto` (text), `fecha` (integer)
   - `destacados`: `usuario_id` (integer), `norma_id` (text), `articulo` (integer)
2. En cada servicio copia `xano.properties.example` como `xano.properties` y pega la **Base URL** de tu API Group.
   Ese archivo esta en `.gitignore`: la URL no se sube al repositorio.
   Alternativa: definir la variable de entorno `XANO_BASE_URL`.

## Ejecutar
```
cd backend/usuarios-service && .\mvnw.cmd spring-boot:run
cd backend/estudio-service  && .\mvnw.cmd spring-boot:run     (en otra terminal)
```
Ejecuta desde la carpeta de cada servicio para que se lea su `xano.properties`.

## Endpoints de los microservicios (los que usa la app)
| Metodo | Ruta | Descripcion | Respuestas |
|---|---|---|---|
| POST | `:8081/api/usuarios/registro` | Crear cuenta | 201 / 400 / 409 |
| POST | `:8081/api/usuarios/login` | Iniciar sesion | 200 / 401 |
| GET | `:8081/api/usuarios/{id}` | Obtener usuario | 200 / 404 |
| PUT | `:8081/api/usuarios/{id}` | Actualizar nombre y tamano de letra | 200 / 404 |
| GET | `:8082/api/notas?usuarioId=` | Listar notas | 200 |
| POST | `:8082/api/notas` | Crear nota | 201 / 400 |
| PUT | `:8082/api/notas/{id}` | Editar nota | 200 / 404 |
| DELETE | `:8082/api/notas/{id}` | Eliminar nota | 204 / 404 |
| GET | `:8082/api/destacados?usuarioId=&normaId=` | Listar destacados | 200 |
| POST | `:8082/api/destacados` | Marcar articulo | 201 |
| DELETE | `:8082/api/destacados?usuarioId=&normaId=&articulo=` | Desmarcar | 204 |

Si Xano no responde, los servicios devuelven **502** y la app muestra su mensaje de error.

## Endpoints de Xano que consumen los servicios
`GET/POST /usuarios`, `GET/PATCH /usuarios/{id}`, `GET/POST /notas`, `GET/PATCH/DELETE /notas/{id}`,
`GET/POST /destacados`, `DELETE /destacados/{id}`.

## Prueba rapida
```
curl -X POST localhost:8081/api/usuarios/registro -H "Content-Type: application/json" \
  -d '{"nombre":"Prueba Uno","correo":"prueba@pocketlaw.cl","clave":"Prueba123"}'
curl -X POST localhost:8082/api/notas -H "Content-Type: application/json" \
  -d '{"usuarioId":1,"normaId":"codigo_civil","texto":"Repasar el articulo 1"}'
```
