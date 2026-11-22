package cl.pocketlaw.app.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cl.pocketlaw.app.data.local.BaseDatos
import cl.pocketlaw.app.data.remote.ClienteRed
import cl.pocketlaw.app.data.repository.EstudioRepositoryImpl
import cl.pocketlaw.app.data.repository.IndicadoresRepositoryImpl
import cl.pocketlaw.app.data.repository.LegislacionRepositoryMock
import cl.pocketlaw.app.data.repository.UsuariosRepositoryImpl

class FabricaViewModel(private val aplicacion: Application) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val usuarios = UsuariosRepositoryImpl(ClienteRed.usuariosApi, BaseDatos.obtener(aplicacion).usuarioDao())
        val estudio = EstudioRepositoryImpl(ClienteRed.estudioApi)
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(usuarios)
            modelClass.isAssignableFrom(EstudioViewModel::class.java) ->
                EstudioViewModel(usuarios, estudio, LegislacionRepositoryMock())
            modelClass.isAssignableFrom(PerfilViewModel::class.java) -> PerfilViewModel(usuarios)
            modelClass.isAssignableFrom(IndicadoresViewModel::class.java) ->
                IndicadoresViewModel(IndicadoresRepositoryImpl(ClienteRed.indicadoresApi))
            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        } as T
    }
}
