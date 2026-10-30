package cl.pocketlaw.app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(usuario: Usuario): Long

    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun porCorreo(correo: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE sesionActiva = 1 LIMIT 1")
    fun sesion(): Flow<Usuario?>

    @Query("UPDATE usuarios SET sesionActiva = 0")
    suspend fun cerrarSesiones()

    @Query("UPDATE usuarios SET sesionActiva = 1 WHERE id = :id")
    suspend fun activar(id: Int)

    @Query("UPDATE usuarios SET fotoUri = :uri WHERE id = :id")
    suspend fun actualizarFoto(id: Int, uri: String)

    @Query("UPDATE usuarios SET tamanoLetra = :tamano WHERE id = :id")
    suspend fun actualizarTamanoLetra(id: Int, tamano: Float)
}

@Dao
interface NotaDao {
    @Insert
    suspend fun insertar(nota: Nota)

    @Delete
    suspend fun eliminar(nota: Nota)

    @Query("SELECT * FROM notas WHERE usuarioId = :usuarioId ORDER BY fecha DESC")
    fun deUsuario(usuarioId: Int): Flow<List<Nota>>
}

@Dao
interface DestacadoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(destacado: Destacado)

    @Query("DELETE FROM destacados WHERE usuarioId = :usuarioId AND normaId = :normaId AND articulo = :articulo")
    suspend fun eliminar(usuarioId: Int, normaId: String, articulo: Int)

    @Query("SELECT * FROM destacados WHERE usuarioId = :usuarioId AND normaId = :normaId")
    fun deNorma(usuarioId: Int, normaId: String): Flow<List<Destacado>>
}
