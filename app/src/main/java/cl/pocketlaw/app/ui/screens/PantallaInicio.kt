@file:OptIn(ExperimentalMaterial3Api::class)

package cl.pocketlaw.app.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.pocketlaw.app.data.model.Norma
import cl.pocketlaw.app.util.Notificaciones
import cl.pocketlaw.app.viewmodel.EstudioViewModel

@Composable
fun PantallaInicio(vm: EstudioViewModel, abrirNorma: (String) -> Unit) {
    val normas by vm.normas.collectAsStateWithLifecycle()
    val busqueda by vm.busqueda.collectAsStateWithLifecycle()
    val nombre by vm.nombreUsuario.collectAsStateWithLifecycle()

    val contexto = LocalContext.current
    var mensajeAviso by remember { mutableStateOf<String?>(null) }

    fun lanzarAviso() {
        val (norma, cambio) = vm.avisos.first()
        Notificaciones.mostrarAviso(contexto, "Modificación en ${norma.nombreCorto}", cambio.descripcion)
    }

    val permisoNotificaciones = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { concedido ->
        if (concedido) {
            mensajeAviso = null
            lanzarAviso()
        } else {
            mensajeAviso = "Permiso de notificaciones denegado. Puedes habilitarlo desde los ajustes del dispositivo."
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column {
                Text("Hola, ${nombre.substringBefore(' ')}", style = MaterialTheme.typography.headlineSmall)
                Text(
                    "¿Qué norma estudiarás hoy?",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Notifications, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                    Spacer(Modifier.width(8.dp))
                    Text("Avisos de modificaciones", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.weight(1f))
                    TextButton(onClick = {
                        if (Notificaciones.tienePermiso(contexto)) {
                            mensajeAviso = null
                            lanzarAviso()
                        } else {
                            permisoNotificaciones.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }) { Text("Probar aviso") }
                }
                mensajeAviso?.let {
                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
                vm.avisos.forEach { (norma, cambio) ->
                    Card(
                        onClick = { abrirNorma(norma.id) },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text("${norma.nombreCorto} · ${cambio.anio}", style = MaterialTheme.typography.labelLarge)
                            Text(cambio.descripcion, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
        item {
            OutlinedTextField(
                value = busqueda,
                onValueChange = vm::cambiarBusqueda,
                label = { Text("Buscar norma o materia") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(
                        selected = busqueda.isEmpty(),
                        onClick = { vm.cambiarBusqueda("") },
                        label = { Text("Todas") }
                    )
                }
                items(vm.materias, key = { it }) { materia ->
                    FilterChip(
                        selected = busqueda == materia,
                        onClick = { vm.cambiarBusqueda(materia) },
                        label = { Text(materia) }
                    )
                }
            }
        }
        if (normas.isEmpty()) {
            item {
                Text(
                    "No se encontraron normas con ese criterio.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        items(normas, key = { it.id }) { norma ->
            TarjetaNorma(norma) { abrirNorma(norma.id) }
        }
    }
}

@Composable
private fun TarjetaNorma(norma: Norma, alPulsar: () -> Unit) {
    Card(
        onClick = alPulsar,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(MaterialTheme.colorScheme.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Gavel, contentDescription = null, tint = MaterialTheme.colorScheme.secondaryContainer)
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(norma.nombre, style = MaterialTheme.typography.titleMedium)
                Surface(shape = RoundedCornerShape(50), color = MaterialTheme.colorScheme.secondaryContainer) {
                    Text(
                        norma.materia,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
                Text(
                    "${norma.articulos.size} artículos · Última modificación: ${norma.modificaciones.maxOf { it.anio }}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
