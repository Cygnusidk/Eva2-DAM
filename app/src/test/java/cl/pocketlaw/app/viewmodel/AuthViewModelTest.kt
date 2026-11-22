package cl.pocketlaw.app.viewmodel

import cl.pocketlaw.app.data.local.Usuario
import cl.pocketlaw.app.data.repository.ResultadoAuth
import cl.pocketlaw.app.data.repository.UsuariosRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private var llamadasIngreso = 0

    @Before
    fun preparar() {
        Dispatchers.setMain(dispatcher)
        llamadasIngreso = 0
    }

    @After
    fun limpiar() {
        Dispatchers.resetMain()
    }

    private fun repositorioFalso(
        ingreso: ResultadoAuth = ResultadoAuth.EXITO,
        registro: ResultadoAuth = ResultadoAuth.EXITO
    ) = object : UsuariosRepository {
        override val sesion: Flow<Usuario?> = flowOf(null)

        override suspend fun registrar(nombre: String, correo: String, clave: String) = registro

        override suspend fun iniciarSesion(correo: String, clave: String): ResultadoAuth {
            llamadasIngreso++
            return ingreso
        }

        override suspend fun cerrarSesion() {}
        override suspend fun actualizarFoto(usuarioId: Long, uri: String) {}
        override suspend fun actualizarTamanoLetra(usuarioId: Long, tamano: Float) {}
        override suspend fun sincronizarPerfil(usuarioId: Long) {}
    }

    @Test
    fun `ingresar con campos vacios no llama al repositorio`() = runTest {
        println("INICIO: comprobar el ingreso con el formulario vacio.")
        val viewModel = AuthViewModel(repositorioFalso())
        var ingreso = false

        viewModel.ingresar { ingreso = true }
        advanceUntilIdle()

        assertEquals(0, llamadasIngreso)
        assertNotNull(viewModel.form.errorCorreo)
        assertFalse(ingreso)
        println("CORRECTO: el formulario vacio no llego al servidor.")
    }

    @Test
    fun `ingresar con datos correctos ejecuta la accion de exito`() = runTest {
        println("INICIO: comprobar el ingreso con datos correctos.")
        val viewModel = AuthViewModel(repositorioFalso(ingreso = ResultadoAuth.EXITO))
        var ingreso = false
        viewModel.cambiarCorreo("prueba@pocketlaw.cl")
        viewModel.cambiarClaveIngreso("Prueba123")

        viewModel.ingresar { ingreso = true }
        advanceUntilIdle()

        assertEquals(1, llamadasIngreso)
        assertTrue(ingreso)
        println("CORRECTO: el ingreso exitoso avanzo a la pantalla principal.")
    }

    @Test
    fun `ingresar sin conexion muestra un mensaje al usuario`() = runTest {
        println("INICIO: comprobar el ingreso cuando el servidor no responde.")
        val viewModel = AuthViewModel(repositorioFalso(ingreso = ResultadoAuth.ERROR))
        var ingreso = false
        viewModel.cambiarCorreo("prueba@pocketlaw.cl")
        viewModel.cambiarClaveIngreso("Prueba123")

        viewModel.ingresar { ingreso = true }
        advanceUntilIdle()

        assertFalse(ingreso)
        assertTrue(viewModel.form.mensaje!!.contains("conectar"))
        assertFalse(viewModel.form.cargando)
        println("CORRECTO: se aviso que no hay conexion con el servidor.")
    }

    @Test
    fun `registrar con un correo repetido muestra el error en el correo`() = runTest {
        println("INICIO: comprobar el registro con un correo que ya existe.")
        val viewModel = AuthViewModel(repositorioFalso(registro = ResultadoAuth.RECHAZADO))
        viewModel.cambiarNombre("Camila Soto")
        viewModel.cambiarCorreo("prueba@pocketlaw.cl")
        viewModel.cambiarClave("Prueba123")
        viewModel.cambiarConfirmacion("Prueba123")

        viewModel.registrar { }
        advanceUntilIdle()

        assertEquals("Este correo ya está registrado", viewModel.form.errorCorreo)
        println("CORRECTO: el correo repetido fue rechazado.")
    }
}
