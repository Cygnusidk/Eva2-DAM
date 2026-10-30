# PocketLaw

PocketLaw es una aplicación móvil para Android orientada al estudio y la consulta de legislación, pensada principalmente para estudiantes de Derecho y abogados. Su propósito es reunir en un solo lugar la lectura de normas, la toma de notas y el seguimiento de las modificaciones legales, de manera que el usuario no tenga que cambiar constantemente entre distintas herramientas para estudiar.

## Integrantes

- Sebastián Pérez
- Felipe Ojeda

## Descripción del funcionamiento

Al abrir la aplicación se muestra una pantalla de carga con el logo animado. Si el usuario ya había iniciado sesión, la aplicación lo lleva directamente a la pantalla principal. En caso contrario, lo dirige al inicio de sesión, desde donde también puede crear una cuenta nueva.

El registro y el inicio de sesión cuentan con formularios que revisan cada campo. Si el usuario ingresa un correo con formato incorrecto, una contraseña muy corta o contraseñas que no coinciden, el campo correspondiente muestra un ícono y un mensaje que explica el problema. Los datos de la cuenta se guardan en el teléfono y la contraseña se almacena cifrada.

Una vez dentro, el usuario encuentra tres secciones en la barra inferior:

- **Inicio:** muestra las normas disponibles, un buscador y filtros por materia. También presenta avisos con las modificaciones más recientes, y un botón que envía una notificación de ejemplo al teléfono.
- **Mis notas:** reúne las notas personales del usuario. Se crean desde un botón que abre una ventana emergente y se eliminan previa confirmación.
- **Perfil:** muestra los datos del usuario, permite tomar una foto de perfil con la cámara y obtener la ubicación actual. Desde esta sección también se cierra la sesión.

Al seleccionar una norma, el usuario accede a su detalle. Allí puede leer los artículos, ajustar el tamaño de la letra (la preferencia queda guardada para la próxima vez), destacar los artículos importantes con una estrella y consultar el material de estudio, que incluye principios, doctrina y jurisprudencia. También puede revisar el historial de modificaciones de la norma, filtrándolo por año, y crear notas asociadas a ella.

Para usar la cámara, la ubicación y las notificaciones, la aplicación solicita primero el permiso correspondiente. Si el usuario no lo concede, se muestra un mensaje que lo informa y el resto de la aplicación continúa funcionando con normalidad.

## Funcionalidades implementadas

- Pantalla de carga con animación y logo de PocketLaw.
- Registro e inicio de sesión con validación de cada campo, íconos y mensajes de error.
- Sesión persistente: el usuario no necesita iniciar sesión cada vez que abre la aplicación.
- Catálogo de seis normas: Constitución, Código Civil, Código Penal, Código del Trabajo, Código Procesal Penal y Ley del Consumidor.
- Buscador de normas y filtro por materia.
- Lectura de artículos con tamaño de letra ajustable y guardado en la base de datos.
- Destacado de artículos.
- Notas personales asociadas a una norma, con creación y eliminación mediante ventanas emergentes.
- Historial de modificaciones de cada norma, filtrable por año.
- Material de estudio por norma: principios, doctrina y jurisprudencia.
- Avisos de modificaciones recientes y notificación de ejemplo.
- Cámara para la foto de perfil, ubicación con GPS y notificaciones.
- Tema claro y oscuro, según la configuración del teléfono.
- Animaciones en las transiciones entre pantallas, en el destacado de artículos y en el historial.

## Arquitectura

La aplicación sigue el patrón MVVM, en el que cada capa tiene una responsabilidad distinta:

```
Pantallas (ui)  →  ViewModel  →  Repositorios  →  Room / Datos de prueba
```

- **Pantallas:** solo muestran información y reciben las acciones del usuario.
- **ViewModel:** guarda el estado de cada pantalla y aplica las validaciones.
- **Repositorios:** son la única puerta de acceso a los datos. `Repositorio` maneja la base de datos y `LegislacionRepository` entrega las normas.
- **Datos:** Room guarda usuarios, notas, destacados y la preferencia de tamaño de letra.

La conexión con la fuente oficial de legislación se simula con `LegislacionRepositoryMock`, que entrega datos de prueba. Como la pantalla solo conoce la interfaz `LegislacionRepository`, esta implementación se puede reemplazar por una que consulte una API real sin cambiar el resto de la aplicación.

## Estructura de carpetas

```
app/src/main/java/cl/pocketlaw/app
├── data
│   ├── local        Entidades, DAOs y base de datos Room
│   ├── model        Catálogo de normas de prueba
│   └── repository   Repositorio local, LegislacionRepository y su versión de prueba
├── viewmodel        ViewModels y fábrica de ViewModels
├── util             Validadores, cifrado, cámara, ubicación y notificaciones
└── ui
    ├── theme        Colores (claro y oscuro) y tipografía
    ├── components   Campo de texto validado, encabezado y ventana de notas
    ├── screens      Pantallas de la aplicación
    └── navigation   Rutas y navegación
```

## Tecnologías y versiones

- Kotlin 2.0.20 y Jetpack Compose (BOM 2024.09.00) con Material 3
- Android Gradle Plugin 8.5.2 y Gradle 8.9
- Room 2.6.1 con KSP 2.0.20-1.0.25
- Navigation Compose 2.8.0 y Lifecycle 2.8.6
- Coil 2.7.0 para mostrar la foto de perfil
- minSdk 26, targetSdk 34 y compileSdk 34
- Android Studio con JetBrains Runtime 25.0.4+-16339643-b508.27 amd64. Gradle se ejecuta con JDK 21.

## Cómo compilar y ejecutar

1. Abrir la carpeta del proyecto en Android Studio desde **File > Open**.
2. Si aparece el aviso de Java, seleccionar JDK 21 y esperar a que termine la sincronización de Gradle.
3. Ejecutar la aplicación en un emulador o en un teléfono con Android 8.0 o superior.
4. Crear una cuenta desde la opción "Regístrate" e iniciar sesión.

Como ejemplo ficticio para probar, se puede registrar el correo `prueba@pocketlaw.cl` con la contraseña `Prueba123`. La aplicación no trae usuarios precargados.

Para probar la ubicación en el emulador, se debe configurar un punto desde **Extended Controls > Location**. La cámara funciona con la cámara virtual del emulador.
