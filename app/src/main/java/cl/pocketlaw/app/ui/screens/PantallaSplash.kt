package cl.pocketlaw.app.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cl.pocketlaw.app.R
import cl.pocketlaw.app.ui.theme.Marino
import cl.pocketlaw.app.viewmodel.EstadoSesion
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PantallaSplash(sesion: EstadoSesion, irALogin: () -> Unit, irAPrincipal: () -> Unit) {
    val escala = remember { Animatable(0.6f) }
    val opacidad = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch { escala.animateTo(1f, tween(900, easing = FastOutSlowInEasing)) }
        opacidad.animateTo(1f, tween(900))
    }

    LaunchedEffect(sesion) {
        if (sesion !is EstadoSesion.Cargando) {
            delay(1500)
            if (sesion is EstadoSesion.Activa) irAPrincipal() else irALogin()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Marino)
            .graphicsLayer { alpha = opacidad.value },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_logo),
            contentDescription = "Logo de PocketLaw",
            modifier = Modifier
                .size(140.dp)
                .graphicsLayer {
                    scaleX = escala.value
                    scaleY = escala.value
                }
        )
        Spacer(Modifier.height(16.dp))
        Text("PocketLaw", style = MaterialTheme.typography.headlineMedium, color = Color.White)
        Text("Legislación al alcance de tu mano", color = Color.White.copy(alpha = 0.8f))
    }
}
