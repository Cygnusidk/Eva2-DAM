package cl.pocketlaw.app.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios", indices = [Index(value = ["correo"], unique = true)])
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val correo: String,
    val claveHash: String,
    val fotoUri: String? = null,
    val tamanoLetra: Float = 16f,
    val sesionActiva: Boolean = false
)

@Entity(tableName = "notas")
data class Nota(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuarioId: Int,
    val normaId: String,
    val texto: String,
    val fecha: Long
)

@Entity(tableName = "destacados", primaryKeys = ["usuarioId", "normaId", "articulo"])
data class Destacado(
    val usuarioId: Int,
    val normaId: String,
    val articulo: Int
)
