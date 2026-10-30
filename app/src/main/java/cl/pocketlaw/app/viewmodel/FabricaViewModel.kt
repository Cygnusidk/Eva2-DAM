package cl.pocketlaw.app.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cl.pocketlaw.app.data.repository.LegislacionRepositoryMock
import cl.pocketlaw.app.data.repository.Repositorio

class FabricaViewModel(private val aplicacion: Application) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repositorio = Repositorio.obtener(aplicacion)
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(repositorio)
            modelClass.isAssignableFrom(EstudioViewModel::class.java) ->
                EstudioViewModel(repositorio, LegislacionRepositoryMock())
            modelClass.isAssignableFrom(PerfilViewModel::class.java) -> PerfilViewModel(repositorio)
            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        } as T
    }
}
