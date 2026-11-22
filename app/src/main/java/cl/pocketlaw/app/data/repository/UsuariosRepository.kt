package cl.pocketlaw.app.data.repository

import cl.pocketlaw.app.data.local.Usuario
import kotlinx.coroutines.flow.Flow


enum class ResultadoAuth { EXITO, RECHAZADO, ERROR }

interface UsuariosRepository {
    val sesion: Flow<Usuario?>

    suspend fun registrar(nombre: String, correo: String, clave: String): ResultadoAuth

    suspend fun iniciarSesion(correo: String, clave: String): ResultadoAuth

    suspend fun cerrarSesion()

    suspend fun actualizarFoto(usuarioId: Long, uri: String)

    suspend fun actualizarTamanoLetra(usuarioId: Long, tamano: Float)

    suspend fun sincronizarPerfil(usuarioId: Long)
}
