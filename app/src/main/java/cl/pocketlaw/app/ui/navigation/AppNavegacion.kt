package cl.pocketlaw.app.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cl.pocketlaw.app.ui.components.rememberFabrica
import cl.pocketlaw.app.ui.screens.PantallaDetalle
import cl.pocketlaw.app.ui.screens.PantallaLogin
import cl.pocketlaw.app.ui.screens.PantallaPrincipal
import cl.pocketlaw.app.ui.screens.PantallaRegistro
import cl.pocketlaw.app.ui.screens.PantallaSplash
import cl.pocketlaw.app.viewmodel.AuthViewModel

object Rutas {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val PRINCIPAL = "principal"
    const val DETALLE = "detalle/{normaId}"

    fun detalle(normaId: String) = "detalle/$normaId"
}

@Composable
fun AppNavegacion() {
    val navegador = rememberNavController()
    val authVm: AuthViewModel = viewModel(factory = rememberFabrica())
    val sesion by authVm.sesion.collectAsStateWithLifecycle()

    NavHost(
        navController = navegador,
        startDestination = Rutas.SPLASH,
        enterTransition = { fadeIn(tween(300)) + slideInHorizontally(tween(300)) { it / 8 } },
        exitTransition = { fadeOut(tween(200)) },
        popEnterTransition = { fadeIn(tween(300)) },
        popExitTransition = { fadeOut(tween(200)) + slideOutHorizontally(tween(300)) { it / 8 } }
    ) {
        composable(Rutas.SPLASH) {
            PantallaSplash(
                sesion = sesion,
                irALogin = {
                    navegador.navigate(Rutas.LOGIN) { popUpTo(Rutas.SPLASH) { inclusive = true } }
                },
                irAPrincipal = {
                    navegador.navigate(Rutas.PRINCIPAL) { popUpTo(Rutas.SPLASH) { inclusive = true } }
                }
            )
        }
        composable(Rutas.LOGIN) {
            PantallaLogin(
                vm = authVm,
                alIngresar = {
                    navegador.navigate(Rutas.PRINCIPAL) { popUpTo(Rutas.LOGIN) { inclusive = true } }
                },
                irARegistro = { navegador.navigate(Rutas.REGISTRO) }
            )
        }
        composable(Rutas.REGISTRO) {
            PantallaRegistro(
                vm = authVm,
                alRegistrar = {
                    navegador.navigate(Rutas.PRINCIPAL) { popUpTo(Rutas.LOGIN) { inclusive = true } }
                },
                volver = { navegador.popBackStack() }
            )
        }
        composable(Rutas.PRINCIPAL) {
            PantallaPrincipal(
                abrirNorma = { navegador.navigate(Rutas.detalle(it)) },
                alSalir = {
                    navegador.navigate(Rutas.LOGIN) { popUpTo(Rutas.PRINCIPAL) { inclusive = true } }
                }
            )
        }
        composable(
            route = Rutas.DETALLE,
            arguments = listOf(navArgument("normaId") { type = NavType.StringType })
        ) { entrada ->
            PantallaDetalle(
                normaId = entrada.arguments?.getString("normaId") ?: "",
                volver = { navegador.popBackStack() }
            )
        }
    }
}
