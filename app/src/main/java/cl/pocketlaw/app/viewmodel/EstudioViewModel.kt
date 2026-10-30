package cl.pocketlaw.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.pocketlaw.app.data.local.Destacado
import cl.pocketlaw.app.data.local.Nota
import cl.pocketlaw.app.data.model.Modificacion
import cl.pocketlaw.app.data.model.Norma
import cl.pocketlaw.app.data.repository.LegislacionRepository
import cl.pocketlaw.app.data.repository.Repositorio
import cl.pocketlaw.app.util.Validadores
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class EstudioViewModel(
    private val repositorio: Repositorio,
    private val legislacion: LegislacionRepository
) : ViewModel() {

    private val usuarioId: StateFlow<Int?> = repositorio.sesion
        .map { it?.id }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val nombreUsuario: StateFlow<String> = repositorio.sesion
        .map { it?.nombre ?: "" }
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")

    private val consulta = MutableStateFlow("")
    val busqueda: StateFlow<String> = consulta

    val normas: StateFlow<List<Norma>> = consulta
        .map { texto ->
            legislacion.normas().filter {
                it.nombre.contains(texto.trim(), ignoreCase = true) ||
                    it.materia.contains(texto.trim(), ignoreCase = true)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, legislacion.normas())

    val notas: StateFlow<List<Nota>> = usuarioId
        .flatMapLatest { id -> if (id == null) flowOf(emptyList()) else repositorio.notas(id) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val catalogo: List<Norma> = legislacion.normas()
    val materias: List<String> = legislacion.materias()
    val avisos: List<Pair<Norma, Modificacion>> = legislacion.modificacionesRecientes(3)

    var tamanoLetra by mutableFloatStateOf(16f)

    var anioFiltro by mutableStateOf<Int?>(null)
        private set

    init {
        viewModelScope.launch {
            tamanoLetra = repositorio.sesion.filterNotNull().first().tamanoLetra
        }
    }

    fun norma(id: String): Norma? = legislacion.norma(id)

    fun filtrarAnio(anio: Int?) {
        anioFiltro = anio
    }

    fun guardarTamanoLetra() {
        val id = usuarioId.value ?: return
        viewModelScope.launch { repositorio.actualizarTamanoLetra(id, tamanoLetra) }
    }

    var textoNota by mutableStateOf("")
        private set
    var errorNota by mutableStateOf<String?>(null)
        private set

    fun cambiarBusqueda(texto: String) {
        consulta.value = texto
    }

    fun destacadosDe(normaId: String): Flow<Set<Int>> = usuarioId.flatMapLatest { id ->
        if (id == null) flowOf(emptySet())
        else repositorio.destacados(id, normaId).map { lista -> lista.map { it.articulo }.toSet() }
    }

    fun alternarDestacado(normaId: String, articulo: Int, activar: Boolean) {
        val id = usuarioId.value ?: return
        viewModelScope.launch {
            repositorio.cambiarDestacado(Destacado(id, normaId, articulo), activar)
        }
    }

    fun limpiarNota() {
        textoNota = ""
        errorNota = null
    }

    fun cambiarTextoNota(valor: String) {
        textoNota = valor
        errorNota = Validadores.nota(valor)
    }

    fun guardarNota(normaId: String, alTerminar: () -> Unit) {
        val error = Validadores.nota(textoNota)
        errorNota = error
        if (error != null) return
        val id = usuarioId.value ?: return
        viewModelScope.launch {
            repositorio.agregarNota(
                Nota(usuarioId = id, normaId = normaId, texto = textoNota.trim(), fecha = System.currentTimeMillis())
            )
            limpiarNota()
            alTerminar()
        }
    }

    fun eliminarNota(nota: Nota) {
        viewModelScope.launch { repositorio.eliminarNota(nota) }
    }
}
