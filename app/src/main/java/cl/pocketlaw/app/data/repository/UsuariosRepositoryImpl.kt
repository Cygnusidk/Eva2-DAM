package cl.pocketlaw.app.data.repository

import cl.pocketlaw.app.data.local.Usuario
import cl.pocketlaw.app.data.local.UsuarioDao
import cl.pocketlaw.app.data.remote.ActualizarUsuarioDto
import cl.pocketlaw.app.data.remote.LoginDto
import cl.pocketlaw.app.data.remote.RegistroDto
import cl.pocketlaw.app.data.remote.UsuarioDto
import cl.pocketlaw.app.data.remote.UsuariosApi
import java.io.IOException
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UsuariosRepositoryImpl(
    private val api: UsuariosApi,
    private val dao: UsuarioDao
) : UsuariosRepository {

    override val sesion: Flow<Usuario?> = dao.sesion()

    override suspend fun registrar(nombre: String, correo: String, clave: String): ResultadoAuth =
        autenticar { api.registrar(RegistroDto(nombre.trim(), correo.trim().lowercase(), clave)) }

    override suspend fun iniciarSesion(correo: String, clave: String): ResultadoAuth =
        autenticar { api.iniciarSesion(LoginDto(correo.trim().lowercase(), clave)) }

    private suspend fun autenticar(llamada: suspend () -> UsuarioDto): ResultadoAuth =
        try {
            guardarSesion(llamada())
            ResultadoAuth.EXITO
        } catch (e: HttpException) {
            if (e.code() == 401 || e.code() == 409) ResultadoAuth.RECHAZADO else ResultadoAuth.ERROR
        } catch (e: IOException) {
            ResultadoAuth.ERROR
        }

    private suspend fun guardarSesion(dto: UsuarioDto) {
        val local = dao.porId(dto.id)
        dao.cerrarSesiones()
        dao.guardar(
            Usuario(
                id = dto.id,
                nombre = dto.nombre,
                correo = dto.correo,
                fotoUri = local?.fotoUri,
                tamanoLetra = dto.tamanoLetra,
                sesionActiva = true
            )
        )
    }

    override suspend fun cerrarSesion() = dao.cerrarSesiones()

    override suspend fun actualizarFoto(usuarioId: Long, uri: String) = dao.actualizarFoto(usuarioId, uri)

    override suspend fun actualizarTamanoLetra(usuarioId: Long, tamano: Float) {
        dao.actualizarTamanoLetra(usuarioId, tamano)
        val usuario = dao.porId(usuarioId) ?: return
        try {
            api.actualizar(usuarioId, ActualizarUsuarioDto(usuario.nombre, tamano))
        } catch (e: HttpException) {
        } catch (e: IOException) {
        }
    }

    override suspend fun sincronizarPerfil(usuarioId: Long) {
        try {
            val remoto = api.obtener(usuarioId)
            val local = dao.porId(usuarioId) ?: return
            dao.guardar(local.copy(nombre = remoto.nombre, tamanoLetra = remoto.tamanoLetra))
        } catch (e: HttpException) {
        } catch (e: IOException) {
        }
    }
}
