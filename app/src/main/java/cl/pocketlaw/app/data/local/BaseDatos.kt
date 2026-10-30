package cl.pocketlaw.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Usuario::class, Nota::class, Destacado::class], version = 2, exportSchema = false)
abstract class BaseDatos : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun notaDao(): NotaDao
    abstract fun destacadoDao(): DestacadoDao

    companion object {
        @Volatile
        private var instancia: BaseDatos? = null

        fun obtener(context: Context): BaseDatos =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    BaseDatos::class.java,
                    "pocketlaw.db"
                ).fallbackToDestructiveMigration().build().also { instancia = it }
            }
    }
}
