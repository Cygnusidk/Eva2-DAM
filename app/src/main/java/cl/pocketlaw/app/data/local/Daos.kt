package cl.pocketlaw.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardar(usuario: Usuario)

    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun porId(id: Long): Usuario?

    @Query("SELECT * FROM usuarios WHERE sesionActiva = 1 LIMIT 1")
    fun sesion(): Flow<Usuario?>

    @Query("UPDATE usuarios SET sesionActiva = 0")
    suspend fun cerrarSesiones()

    @Query("UPDATE usuarios SET fotoUri = :uri WHERE id = :id")
    suspend fun actualizarFoto(id: Long, uri: String)

    @Query("UPDATE usuarios SET tamanoLetra = :tamano WHERE id = :id")
    suspend fun actualizarTamanoLetra(id: Long, tamano: Float)
}
