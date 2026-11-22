package cl.pocketlaw.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey val id: Long,
    val nombre: String,
    val correo: String,
    val fotoUri: String? = null,
    val tamanoLetra: Float = 16f,
    val sesionActiva: Boolean = false
)
