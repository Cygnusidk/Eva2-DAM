package cl.pocketlaw.app.ui.components

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import cl.pocketlaw.app.viewmodel.FabricaViewModel

@Composable
fun rememberFabrica(): FabricaViewModel {
    val aplicacion = LocalContext.current.applicationContext as Application
    return remember { FabricaViewModel(aplicacion) }
}
