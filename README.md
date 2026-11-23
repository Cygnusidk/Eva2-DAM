# PocketLaw

PocketLaw es una aplicación móvil para Android orientada al estudio y la consulta de legislación, pensada principalmente para estudiantes de Derecho y abogados. Su propósito es reunir en un solo lugar la lectura de normas, la toma de notas y el seguimiento de las modificaciones legales, de manera que el usuario no tenga que cambiar constantemente entre distintas herramientas para estudiar.

El proyecto está formado por dos partes:

- **App móvil** (carpeta `app`): Kotlin con Jetpack Compose.
- **Microservicios** (carpeta `backend`): dos servicios en Spring Boot que guardan sus datos en una base de datos en la nube (Xano).

## Integrantes

- Sebastián Pérez
- Felipe Ojeda

## Descripción del funcionamiento

Al abrir la aplicación se muestra una pantalla de carga con el logo animado. Si el usuario ya había iniciado sesión, la aplicación lo lleva directamente a la pantalla principal. En caso contrario, lo dirige al inicio de sesión, desde donde también puede crear una cuenta nueva.

El registro y el inicio de sesión cuentan con formularios que revisan cada campo. Si el usuario ingresa un correo con formato incorrecto, una contraseña muy corta o contraseñas que no coinciden, el campo correspondiente muestra un ícono y un mensaje que explica el problema. Las cuentas se crean y se validan en el microservicio de usuarios, donde la contraseña se guarda cifrada con BCrypt. En el teléfono solo queda la sesión activa, para no pedir el ingreso cada vez.

Una vez dentro, el usuario encuentra tres secciones en la barra inferior:

- **Inicio:** muestra una tarjeta con los valores de la UF, la UTM y el dólar (API externa), las normas disponibles, un buscador y filtros por materia. También presenta avisos con las modificaciones más recientes y un botón que envía una notificación de ejemplo al teléfono.
- **Mis notas:** reúne las notas personales del usuario, que se guardan en el microservicio de estudio. Se crean desde un botón que abre una ventana emergente, se pueden editar y se eliminan previa confirmación.
- **Perfil:** muestra los datos del usuario, permite tomar una foto de perfil con la cámara y obtener la ubicación actual. Desde esta sección también se cierra la sesión.

Al seleccionar una norma, el usuario accede a su detalle. Allí puede leer los artículos, ajustar el tamaño de la letra (la preferencia queda guardada en su cuenta), destacar los artículos importantes con una estrella y consultar el material de estudio, que incluye principios, doctrina y jurisprudencia. También puede revisar el historial de modificaciones de la norma, filtrándolo por año, y crear notas asociadas a ella.

Si el servidor no responde, la aplicación avisa con un mensaje y no se cierra. El destacado de artículos se actualiza de inmediato en pantalla y, si el servidor falla, vuelve al estado anterior.

## Funcionalidades implementadas

- Pantalla de carga con animación y logo de PocketLaw.
- Registro e inicio de sesión contra el microservicio de usuarios, con validación de cada campo, íconos y mensajes de error.
- Sesión persistente en el teléfono con Room.
- Catálogo de seis normas: Constitución, Código Civil, Código Penal, Código del Trabajo, Código Procesal Penal y Ley del Consumidor.
- Buscador de normas y filtro por materia.
- Lectura de artículos con tamaño de letra ajustable, sincronizado con la cuenta del usuario.
- Destacado de artículos guardado en el microservicio.
- CRUD completo de notas personales (crear, listar, editar y eliminar).
- Historial de modificaciones de cada norma, filtrable por año.
- Material de estudio por norma: principios, doctrina y jurisprudencia.
- Tarjeta con UF, UTM y dólar obtenidos de una API externa.
- Avisos de modificaciones recientes y notificación de ejemplo.
- Recursos nativos: cámara (foto de perfil), GPS (ubicación) y notificaciones.
- Tema claro y oscuro, según la configuración del teléfono.
- Animaciones en las transiciones entre pantallas, en el destacado de artículos, en el historial y en la tarjeta de valores.
- Ícono personalizado de la aplicación.

## Arquitectura

La aplicación sigue el patrón MVVM, en el que cada capa tiene una responsabilidad distinta:

```
Pantallas (ui)  →  ViewModel  →  Repositorios  →  Retrofit (microservicios y API externa)
                                              →  Room (sesión del usuario)
                                              →  Datos de prueba (catálogo de normas)
```

- **Pantallas:** solo muestran información y reciben las acciones del usuario.
- **ViewModel:** guarda el estado de cada pantalla y aplica las validaciones.
- **Repositorios:** son la única puerta de acceso a los datos. Cada uno tiene una interfaz (`UsuariosRepository`, `EstudioRepository`, `IndicadoresRepository`, `LegislacionRepository`) y una implementación, lo que permite reemplazarlos por versiones falsas en las pruebas unitarias.
- **Datos:** Room guarda solo la sesión del usuario. Todo lo demás se consulta a los microservicios.

La conexión con la fuente oficial de legislación se simula con `LegislacionRepositoryMock`, que entrega datos de prueba.

## Estructura de carpetas

```
PocketLaw
├── app/src/main/java/cl/pocketlaw/app
│   ├── data
│   │   ├── local        Entidad, DAO y base de datos Room (sesión)
│   │   ├── remote       Interfaces Retrofit, DTOs y cliente de red
│   │   ├── model        Modelos y catálogo de normas de prueba
│   │   └── repository   Interfaces de repositorio y sus implementaciones
│   ├── viewmodel        ViewModels y fábrica de ViewModels
│   ├── util             Validadores, cámara, ubicación y notificaciones
│   └── ui
│       ├── theme        Colores (claro y oscuro) y tipografía
│       ├── components   Campo de texto validado, ventana de notas y tarjeta de valores
│       ├── screens      Pantallas de la aplicación
│       └── navigation   Rutas y navegación
├── app/src/test         Pruebas unitarias
└── backend
    ├── usuarios-service Microservicio de usuarios (puerto 8081)
    └── estudio-service  Microservicio de notas y destacados (puerto 8082)
```

Cada microservicio se organiza en capas, dentro de `src/main/java`:

| Carpeta | Responsabilidad |
|---|---|
| `controller` | Recibe las peticiones HTTP y responde. No tiene lógica |
| `service` | Aplica las reglas (correo repetido, cifrar la clave, nota inexistente) |
| `repository` | Consulta y guarda los datos en las tablas de Xano mediante su API REST |
| `model` | Datos que se leen de Xano y objetos de entrada y salida |
| `xano` | Configuración del cliente HTTP hacia Xano y manejo de errores (502 si Xano no responde) |

## Endpoints utilizados

### Microservicio de usuarios (`usuarios-service`, puerto 8081)

| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/usuarios/registro` | Crea una cuenta (409 si el correo ya existe) |
| POST | `/api/usuarios/login` | Inicia sesión (401 si las credenciales son incorrectas) |
| GET | `/api/usuarios/{id}` | Obtiene los datos de un usuario |
| PUT | `/api/usuarios/{id}` | Actualiza el nombre y el tamaño de letra |

### Microservicio de estudio (`estudio-service`, puerto 8082)

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/notas?usuarioId=` | Lista las notas de un usuario |
| POST | `/api/notas` | Crea una nota |
| PUT | `/api/notas/{id}` | Edita el texto de una nota |
| DELETE | `/api/notas/{id}` | Elimina una nota |
| GET | `/api/destacados?usuarioId=&normaId=` | Lista los artículos destacados de una norma |
| POST | `/api/destacados` | Marca un artículo como destacado |
| DELETE | `/api/destacados?usuarioId=&normaId=&articulo=` | Quita un destacado |

### API externa (mindicador.cl)

| Método | URL | Descripción |
|---|---|---|
| GET | `https://mindicador.cl/api` | Valores de la UF, la UTM y el dólar |

En total son 11 endpoints propios y 1 externo. No requiere llave de acceso.

## Tecnologías y versiones

- Kotlin 2.0.20 y Jetpack Compose (BOM 2024.09.00) con Material 3
- Android Gradle Plugin 8.5.2 y Gradle 8.9
- Room 2.6.1 con KSP 2.0.20-1.0.25
- Retrofit 2.11.0 con Gson y OkHttp 4.12.0
- Navigation Compose 2.8.0 y Lifecycle 2.8.6
- Coil 2.7.0 para mostrar la foto de perfil
- Pruebas: JUnit 4.13.2, kotlinx-coroutines-test 1.9.0 y MockWebServer 4.12.0
- Microservicios: Spring Boot 3.3.5, Java 17 y Xano (base de datos en la nube, consumida con RestClient)
- minSdk 26, targetSdk 34 y compileSdk 34
- Android Studio con JetBrains Runtime 25.0.4+-16339643-b508.27 amd64. Gradle se ejecuta con JDK 21.

## Cómo ejecutar

### 1. Levantar los microservicios

Se necesita JDK 17 y Maven (o abrir cada carpeta en IntelliJ). En dos terminales distintas:

```
cd backend/usuarios-service
.\mvnw.cmd spring-boot:run 

cd backend/estudio-service
.\mvnw.cmd spring-boot:run 
```

Los datos se guardan en Xano, así que se necesita conexión a internet. Cada servicio lee la dirección de su base en el archivo `xano.properties` (se encuentra junto al `pom.xml` de cada servicio y no se sube al repositorio público; se puede copiar desde `xano.properties.example`). También sirve la variable de entorno `XANO_BASE_URL`. Hay que ejecutar los comandos desde la carpeta de cada servicio para que se lea ese archivo.

Las tablas de Xano son `usuarios` (`nombre`, `correo`, `clave_hash`, `tamano_letra`), `notas` (`usuario_id`, `norma_id`, `texto`, `fecha`) y `destacados` (`usuario_id`, `norma_id`, `articulo`). Los datos se pueden revisar directamente en el panel de Xano.

### 2. Ejecutar la aplicación

1. Abrir la carpeta del proyecto en Android Studio desde **File > Open**.
2. Si aparece el aviso de Java, seleccionar JDK 21 y esperar a que termine la sincronización de Gradle.
3. Ejecutar la aplicación en un emulador con Android 8.0 o superior. El emulador se conecta al computador con la dirección `10.0.2.2`, que ya viene configurada.
4. Crear una cuenta desde la opción "Regístrate" e iniciar sesión.

Para usar un **teléfono real**, el teléfono y el computador deben estar en la misma red wifi. Hay que cambiar la línea `backendHost` del archivo `gradle.properties` por la IP del computador (por ejemplo `192.168.1.50`) y volver a compilar. En Windows se puede ver con el comando `ipconfig`.

Como ejemplo ficticio para probar, se puede registrar el correo `prueba@pocketlaw.cl` con la contraseña `Prueba123`. La aplicación no trae usuarios precargados.

Para probar la ubicación en el emulador, se debe configurar un punto desde **Extended Controls > Location**. La cámara funciona con la cámara virtual del emulador.

## Pruebas unitarias

Las pruebas están en `app/src/test` y siguen los tres pasos de preparación, acción y comprobación:

| Archivo | Qué comprueba |
|---|---|
| `util/ValidadoresTest.kt` | La validación del correo y de la contraseña |
| `viewmodel/AuthViewModelTest.kt` | El inicio de sesión y el registro, con un repositorio falso: formulario vacío, ingreso correcto, sin conexión y correo repetido |
| `data/EstudioRepositoryImplTest.kt` | Las llamadas de las notas con Retrofit y un servidor local simulado (MockWebServer): GET, POST y error 500 |

El ViewModel se prueba con un repositorio falso que no usa internet ni base de datos. El repositorio se prueba con `MockWebServer`, que responde lo que le indiquemos sin necesitar los microservicios.

Para ejecutarlas desde la terminal:

```
.\gradlew.bat :app:testDebugUnitTest --rerun-tasks
```

En Android Studio también se pueden ejecutar con clic derecho sobre la carpeta `app/src/test` y **Run**. La terminal muestra `PASSED` o `FAILED` por cada prueba, junto con los mensajes de cada paso.

## APK firmado

El APK se firma con una llave propia (`pocketlaw.jks`, ubicada en la raíz del proyecto). La configuración está en `app/build.gradle.kts`, dentro del bloque `signingConfigs`.

Para generarlo:

```
./gradlew assembleRelease
```

El archivo queda en `app/build/outputs/apk/release/app-release.apk`.


