package cl.pocketlaw.app.viewmodel

import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.pocketlaw.app.data.local.Usuario
import cl.pocketlaw.app.data.repository.Repositorio
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PerfilViewModel(private val repositorio: Repositorio) : ViewModel() {

    val usuario: StateFlow<Usuario?> = repositorio.sesion
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    var mensaje by mutableStateOf<String?>(null)
        private set
    var coordenadas by mutableStateOf<Pair<Double, Double>?>(null)
        private set
    var buscandoUbicacion by mutableStateOf(false)
        private set

    fun informar(texto: String?) {
        mensaje = texto
    }

    fun iniciarBusquedaUbicacion() {
        buscandoUbicacion = true
        mensaje = null
    }

    fun resultadoUbicacion(ubicacion: Location?) {
        buscandoUbicacion = false
        if (ubicacion == null) {
            mensaje = "No se pudo obtener la ubicación. Verifica que el GPS esté activado."
        } else {
            coordenadas = ubicacion.latitude to ubicacion.longitude
        }
    }

    fun guardarFoto(uri: String) {
        val id = usuario.value?.id ?: return
        viewModelScope.launch {
            repositorio.actualizarFoto(id, uri)
            mensaje = null
        }
    }

    fun cerrarSesion(alTerminar: () -> Unit) {
        viewModelScope.launch {
            repositorio.cerrarSesion()
            alTerminar()
        }
    }
}
