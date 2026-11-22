package cl.pocketlaw.app.data.repository

import cl.pocketlaw.app.data.model.Nota

interface EstudioRepository {
    suspend fun notas(usuarioId: Long): Result<List<Nota>>

    suspend fun crearNota(usuarioId: Long, normaId: String, texto: String): Result<Nota>

    suspend fun editarNota(id: Long, texto: String): Result<Nota>

    suspend fun eliminarNota(id: Long): Result<Unit>

    suspend fun destacados(usuarioId: Long, normaId: String): Result<Set<Int>>

    suspend fun marcarDestacado(usuarioId: Long, normaId: String, articulo: Int): Result<Unit>

    suspend fun desmarcarDestacado(usuarioId: Long, normaId: String, articulo: Int): Result<Unit>
}
