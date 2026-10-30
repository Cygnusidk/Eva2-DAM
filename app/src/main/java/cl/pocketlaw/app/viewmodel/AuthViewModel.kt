package cl.pocketlaw.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.pocketlaw.app.data.local.Usuario
import cl.pocketlaw.app.data.repository.Repositorio
import cl.pocketlaw.app.util.Validadores
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface EstadoSesion {
    data object Cargando : EstadoSesion
    data object SinSesion : EstadoSesion
    data class Activa(val usuario: Usuario) : EstadoSesion
}

data class FormAuth(
    val nombre: String = "",
    val correo: String = "",
    val clave: String = "",
    val confirmacion: String = "",
    val errorNombre: String? = null,
    val errorCorreo: String? = null,
    val errorClave: String? = null,
    val errorConfirmacion: String? = null,
    val mensaje: String? = null,
    val cargando: Boolean = false
)

class AuthViewModel(private val repositorio: Repositorio) : ViewModel() {

    val sesion: StateFlow<EstadoSesion> = repositorio.sesion
        .map<Usuario?, EstadoSesion> { if (it == null) EstadoSesion.SinSesion else EstadoSesion.Activa(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, EstadoSesion.Cargando)

    var form by mutableStateOf(FormAuth())
        private set

    fun limpiar() {
        form = FormAuth()
    }

    fun cambiarNombre(valor: String) {
        form = form.copy(nombre = valor, errorNombre = Validadores.nombre(valor), mensaje = null)
    }

    fun cambiarCorreo(valor: String) {
        form = form.copy(correo = valor, errorCorreo = Validadores.correo(valor), mensaje = null)
    }

    fun cambiarClave(valor: String) {
        val errorConfirmacion =
            if (form.confirmacion.isEmpty()) null else Validadores.confirmacion(valor, form.confirmacion)
        form = form.copy(
            clave = valor,
            errorClave = Validadores.clave(valor),
            errorConfirmacion = errorConfirmacion,
            mensaje = null
        )
    }

    fun cambiarClaveIngreso(valor: String) {
        form = form.copy(clave = valor, errorClave = Validadores.claveIngreso(valor), mensaje = null)
    }

    fun cambiarConfirmacion(valor: String) {
        form = form.copy(
            confirmacion = valor,
            errorConfirmacion = Validadores.confirmacion(form.clave, valor),
            mensaje = null
        )
    }

    fun ingresar(alExito: () -> Unit) {
        val errorCorreo = Validadores.correo(form.correo)
        val errorClave = Validadores.claveIngreso(form.clave)
        form = form.copy(errorCorreo = errorCorreo, errorClave = errorClave)
        if (errorCorreo != null || errorClave != null) return

        viewModelScope.launch {
            form = form.copy(cargando = true, mensaje = null)
            val correcto = repositorio.iniciarSesion(form.correo, form.clave)
            form = form.copy(
                cargando = false,
                mensaje = if (correcto) null else "Correo o contraseña incorrectos"
            )
            if (correcto) alExito()
        }
    }

    fun registrar(alExito: () -> Unit) {
        val errorNombre = Validadores.nombre(form.nombre)
        val errorCorreo = Validadores.correo(form.correo)
        val errorClave = Validadores.clave(form.clave)
        val errorConfirmacion = Validadores.confirmacion(form.clave, form.confirmacion)
        form = form.copy(
            errorNombre = errorNombre,
            errorCorreo = errorCorreo,
            errorClave = errorClave,
            errorConfirmacion = errorConfirmacion
        )
        if (listOf(errorNombre, errorCorreo, errorClave, errorConfirmacion).any { it != null }) return

        viewModelScope.launch {
            form = form.copy(cargando = true, mensaje = null)
            val creado = repositorio.registrar(form.nombre, form.correo, form.clave)
            form = form.copy(
                cargando = false,
                errorCorreo = if (creado) null else "Este correo ya está registrado"
            )
            if (creado) alExito()
        }
    }
}
