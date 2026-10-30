package cl.pocketlaw.app.data.repository

import android.content.Context
import cl.pocketlaw.app.data.local.BaseDatos
import cl.pocketlaw.app.data.local.Destacado
import cl.pocketlaw.app.data.local.Nota
import cl.pocketlaw.app.data.local.Usuario
import cl.pocketlaw.app.util.Seguridad
import kotlinx.coroutines.flow.Flow

class Repositorio(private val db: BaseDatos) {

    val sesion: Flow<Usuario?> = db.usuarioDao().sesion()

    suspend fun registrar(nombre: String, correo: String, clave: String): Boolean {
        val correoLimpio = correo.trim().lowercase()
        if (db.usuarioDao().porCorreo(correoLimpio) != null) return false
        val id = db.usuarioDao().insertar(
            Usuario(nombre = nombre.trim(), correo = correoLimpio, claveHash = Seguridad.hash(clave))
        )
        db.usuarioDao().cerrarSesiones()
        db.usuarioDao().activar(id.toInt())
        return true
    }

    suspend fun iniciarSesion(correo: String, clave: String): Boolean {
        val usuario = db.usuarioDao().porCorreo(correo.trim().lowercase()) ?: return false
        if (usuario.claveHash != Seguridad.hash(clave)) return false
        db.usuarioDao().cerrarSesiones()
        db.usuarioDao().activar(usuario.id)
        return true
    }

    suspend fun cerrarSesion() = db.usuarioDao().cerrarSesiones()

    suspend fun actualizarFoto(usuarioId: Int, uri: String) = db.usuarioDao().actualizarFoto(usuarioId, uri)

    suspend fun actualizarTamanoLetra(usuarioId: Int, tamano: Float) =
        db.usuarioDao().actualizarTamanoLetra(usuarioId, tamano)

    fun notas(usuarioId: Int): Flow<List<Nota>> = db.notaDao().deUsuario(usuarioId)

    suspend fun agregarNota(nota: Nota) = db.notaDao().insertar(nota)

    suspend fun eliminarNota(nota: Nota) = db.notaDao().eliminar(nota)

    fun destacados(usuarioId: Int, normaId: String): Flow<List<Destacado>> =
        db.destacadoDao().deNorma(usuarioId, normaId)

    suspend fun cambiarDestacado(destacado: Destacado, activo: Boolean) {
        if (activo) {
            db.destacadoDao().insertar(destacado)
        } else {
            db.destacadoDao().eliminar(destacado.usuarioId, destacado.normaId, destacado.articulo)
        }
    }

    companion object {
        @Volatile
        private var instancia: Repositorio? = null

        fun obtener(context: Context): Repositorio =
            instancia ?: synchronized(this) {
                instancia ?: Repositorio(BaseDatos.obtener(context)).also { instancia = it }
            }
    }
}
