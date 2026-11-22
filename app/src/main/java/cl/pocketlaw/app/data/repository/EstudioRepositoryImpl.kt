package cl.pocketlaw.app.data.repository

import cl.pocketlaw.app.data.model.Nota
import cl.pocketlaw.app.data.remote.DestacadoNuevoDto
import cl.pocketlaw.app.data.remote.EstudioApi
import cl.pocketlaw.app.data.remote.NotaNuevaDto
import cl.pocketlaw.app.data.remote.NotaTextoDto
import cl.pocketlaw.app.data.remote.llamarApi
import retrofit2.HttpException
import retrofit2.Response

class EstudioRepositoryImpl(private val api: EstudioApi) : EstudioRepository {

    override suspend fun notas(usuarioId: Long): Result<List<Nota>> =
        llamarApi { api.notas(usuarioId) }

    override suspend fun crearNota(usuarioId: Long, normaId: String, texto: String): Result<Nota> =
        llamarApi { api.crearNota(NotaNuevaDto(usuarioId, normaId, texto)) }

    override suspend fun editarNota(id: Long, texto: String): Result<Nota> =
        llamarApi { api.editarNota(id, NotaTextoDto(texto)) }

    override suspend fun eliminarNota(id: Long): Result<Unit> =
        llamarApi { verificar(api.eliminarNota(id)) }

    override suspend fun destacados(usuarioId: Long, normaId: String): Result<Set<Int>> =
        llamarApi { api.destacados(usuarioId, normaId).map { it.articulo }.toSet() }

    override suspend fun marcarDestacado(usuarioId: Long, normaId: String, articulo: Int): Result<Unit> =
        llamarApi { api.marcarDestacado(DestacadoNuevoDto(usuarioId, normaId, articulo)); Unit }

    override suspend fun desmarcarDestacado(usuarioId: Long, normaId: String, articulo: Int): Result<Unit> =
        llamarApi { verificar(api.desmarcarDestacado(usuarioId, normaId, articulo)) }

    private fun verificar(respuesta: Response<Unit>) {
        if (!respuesta.isSuccessful) throw HttpException(respuesta)
    }
}
