package cl.pocketlaw.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.pocketlaw.app.data.model.Indicadores
import cl.pocketlaw.app.data.repository.IndicadoresRepository
import kotlinx.coroutines.launch

sealed interface EstadoIndicadores {
    data object Cargando : EstadoIndicadores
    data class Listo(val indicadores: Indicadores) : EstadoIndicadores
    data object Error : EstadoIndicadores
}

class IndicadoresViewModel(private val repositorio: IndicadoresRepository) : ViewModel() {

    var estado by mutableStateOf<EstadoIndicadores>(EstadoIndicadores.Cargando)
        private set

    init {
        cargar()
    }

    fun cargar() {
        viewModelScope.launch {
            estado = EstadoIndicadores.Cargando
            estado = repositorio.obtener().fold(
                onSuccess = { EstadoIndicadores.Listo(it) },
                onFailure = { EstadoIndicadores.Error }
            )
        }
    }
}
