@file:OptIn(ExperimentalMaterial3Api::class)

package cl.pocketlaw.app.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.pocketlaw.app.ui.components.DialogoNota
import cl.pocketlaw.app.ui.components.rememberFabrica
import cl.pocketlaw.app.ui.theme.Marino
import cl.pocketlaw.app.viewmodel.EstudioViewModel
import cl.pocketlaw.app.viewmodel.PerfilViewModel

@Composable
fun PantallaPrincipal(
    abrirNorma: (String) -> Unit,
    alSalir: () -> Unit,
    estudioVm: EstudioViewModel = viewModel(factory = rememberFabrica()),
    perfilVm: PerfilViewModel = viewModel(factory = rememberFabrica())
) {
    var pestana by rememberSaveable { mutableIntStateOf(0) }
    var mostrarDialogo by remember { mutableStateOf(false) }

    val titulos = listOf("Inicio", "Mis notas", "Perfil")
    val iconos = listOf(Icons.Default.Home, Icons.Default.EditNote, Icons.Default.Person)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(titulos[pestana], fontFamily = FontFamily.Serif) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Marino,
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar {
                titulos.forEachIndexed { indice, titulo ->
                    NavigationBarItem(
                        selected = pestana == indice,
                        onClick = { pestana = indice },
                        icon = { Icon(iconos[indice], contentDescription = titulo) },
                        label = { Text(titulo) }
                    )
                }
            }
        },
        floatingActionButton = {
            if (pestana == 1) {
                FloatingActionButton(onClick = { mostrarDialogo = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar nota")
                }
            }
        }
    ) { relleno ->
        AnimatedContent(
            targetState = pestana,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            modifier = Modifier.padding(relleno),
            label = "cambioPestana"
        ) { destino ->
            when (destino) {
                0 -> PantallaInicio(estudioVm, abrirNorma)
                1 -> PantallaNotas(estudioVm)
                else -> PantallaPerfil(perfilVm, alSalir)
            }
        }
    }

    if (mostrarDialogo) {
        DialogoNota(normaFija = null, vm = estudioVm, cerrar = { mostrarDialogo = false })
    }
}
