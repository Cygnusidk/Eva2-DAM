package cl.pocketlaw.app.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.pocketlaw.app.data.model.Modificacion
import cl.pocketlaw.app.data.model.Nota
import cl.pocketlaw.app.data.model.Norma
import cl.pocketlaw.app.data.repository.EstudioRepository
import cl.pocketlaw.app.data.repository.LegislacionRepository
import cl.pocketlaw.app.data.repository.UsuariosRepository
import cl.pocketlaw.app.util.Validadores
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EstudioViewModel(
    private val usuarios: UsuariosRepository,
    private val estudio: EstudioRepository,
    private val legislacion: LegislacionRepository
) : ViewModel() {

    private val mensajeSinConexion = "No se pudo conectar con el servidor. Revisa tu conexión."

    private val usuarioId: StateFlow<Long?> = usuarios.sesion
        .map { it?.id }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val nombreUsuario: StateFlow<String> = usuarios.sesion
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

    val catalogo: List<Norma> = legislacion.normas()
    val materias: List<String> = legislacion.materias()
    val avisos: List<Pair<Norma, Modificacion>> = legislacion.modificacionesRecientes(3)

    private val listaNotas = MutableStateFlow<List<Nota>>(emptyList())
    val notas: StateFlow<List<Nota>> = listaNotas

    var cargandoNotas by mutableStateOf(false)
        private set

    var mensajeError by mutableStateOf<String?>(null)
        private set

    var tamanoLetra by mutableFloatStateOf(16f)

    var anioFiltro by mutableStateOf<Int?>(null)
        private set

    init {
        viewModelScope.launch {
            tamanoLetra = usuarios.sesion.filterNotNull().first().tamanoLetra
        }
        viewModelScope.launch {
            usuarioId.filterNotNull().collect { cargarNotas() }
        }
    }

    fun cargarNotas() {
        val id = usuarioId.value ?: return
        viewModelScope.launch {
            cargandoNotas = true
            estudio.notas(id)
                .onSuccess {
                    listaNotas.value = it
                    mensajeError = null
                }
                .onFailure { mensajeError = mensajeSinConexion }
            cargandoNotas = false
        }
    }

    fun norma(id: String): Norma? = legislacion.norma(id)

    fun filtrarAnio(anio: Int?) {
        anioFiltro = anio
    }

    fun cambiarBusqueda(texto: String) {
        consulta.value = texto
    }

    fun descartarError() {
        mensajeError = null
    }

    fun guardarTamanoLetra() {
        val id = usuarioId.value ?: return
        viewModelScope.launch { usuarios.actualizarTamanoLetra(id, tamanoLetra) }
    }

    var textoNota by mutableStateOf("")
        private set
    var errorNota by mutableStateOf<String?>(null)
        private set
    var notaEnEdicion by mutableStateOf<Nota?>(null)
        private set
    var guardandoNota by mutableStateOf(false)
        private set

    fun limpiarNota() {
        textoNota = ""
        errorNota = null
        notaEnEdicion = null
    }

    fun iniciarEdicion(nota: Nota) {
        notaEnEdicion = nota
        textoNota = nota.texto
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
        val enEdicion = notaEnEdicion
        val texto = textoNota.trim()

        viewModelScope.launch {
            guardandoNota = true
            val resultado = if (enEdicion == null) {
                estudio.crearNota(id, normaId, texto)
            } else {
                estudio.editarNota(enEdicion.id, texto)
            }
            resultado
                .onSuccess { guardada ->
                    val sinLaNota = listaNotas.value.filter { it.id != guardada.id }
                    listaNotas.value = (sinLaNota + guardada).sortedByDescending { it.fecha }
                    limpiarNota()
                    alTerminar()
                }
                .onFailure { errorNota = "No se pudo guardar la nota. Revisa tu conexión." }
            guardandoNota = false
        }
    }

    fun eliminarNota(nota: Nota) {
        viewModelScope.launch {
            estudio.eliminarNota(nota.id)
                .onSuccess { listaNotas.value = listaNotas.value.filter { it.id != nota.id } }
                .onFailure { mensajeError = "No se pudo eliminar la nota. Revisa tu conexión." }
        }
    }

    private val destacadosPorNorma = MutableStateFlow<Map<String, Set<Int>>>(emptyMap())

    fun destacadosDe(normaId: String): Flow<Set<Int>> =
        destacadosPorNorma.map { it[normaId] ?: emptySet() }

    fun cargarDestacados(normaId: String) {
        viewModelScope.launch {
            val id = usuarioId.filterNotNull().first()
            estudio.destacados(id, normaId)
                .onSuccess { destacadosPorNorma.value = destacadosPorNorma.value + (normaId to it) }
                .onFailure { mensajeError = mensajeSinConexion }
        }
    }

    fun alternarDestacado(normaId: String, articulo: Int, activar: Boolean) {
        val id = usuarioId.value ?: return
        val anteriores = destacadosPorNorma.value[normaId] ?: emptySet()
        // se actualiza la pantalla altiro y si el servidor falla se devuelve al estado anterior
        val nuevos = if (activar) anteriores + articulo else anteriores - articulo
        destacadosPorNorma.value = destacadosPorNorma.value + (normaId to nuevos)

        viewModelScope.launch {
            val resultado = if (activar) {
                estudio.marcarDestacado(id, normaId, articulo)
            } else {
                estudio.desmarcarDestacado(id, normaId, articulo)
            }
            resultado.onFailure {
                destacadosPorNorma.value = destacadosPorNorma.value + (normaId to anteriores)
                mensajeError = "No se pudo actualizar el destacado. Revisa tu conexión."
            }
        }
    }
}
