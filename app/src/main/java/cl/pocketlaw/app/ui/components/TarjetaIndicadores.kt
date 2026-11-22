package cl.pocketlaw.app.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.pocketlaw.app.viewmodel.EstadoIndicadores
import java.util.Locale

@Composable
fun TarjetaIndicadores(estado: EstadoIndicadores, reintentar: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Valores de referencia", style = MaterialTheme.typography.titleMedium)

            AnimatedContent(
                targetState = estado,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "estadoIndicadores"
            ) { actual ->
                when (actual) {
                    EstadoIndicadores.Cargando -> Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                        Text("  Consultando valores...", style = MaterialTheme.typography.bodyMedium)
                    }

                    EstadoIndicadores.Error -> Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                        Text(
                            "  No se pudieron obtener los valores.",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = reintentar) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text(" Reintentar")
                        }
                    }

                    is EstadoIndicadores.Listo -> Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        FilaIndicador("UF", actual.indicadores.uf)
                        FilaIndicador("UTM", actual.indicadores.utm)
                        FilaIndicador("Dólar", actual.indicadores.dolar)
                        Text(
                            "Fuente: mindicador.cl · ${actual.indicadores.fecha}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FilaIndicador(nombre: String, valor: Double) {
    Row(Modifier.fillMaxWidth()) {
        Text(nombre, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Text(
            String.format(Locale("es", "CL"), "$%,.2f", valor),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
