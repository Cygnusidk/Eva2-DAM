@file:OptIn(ExperimentalMaterial3Api::class)

package cl.pocketlaw.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.pocketlaw.app.data.local.Nota
import cl.pocketlaw.app.viewmodel.EstudioViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PantallaNotas(vm: EstudioViewModel) {
    val notas by vm.notas.collectAsStateWithLifecycle()
    var notaAEliminar by remember { mutableStateOf<Nota?>(null) }
    val formato = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("es", "CL")) }

    if (notas.isEmpty()) {
        Box(Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(
                    Icons.Default.EditNote,
                    contentDescription = null,
                    modifier = Modifier.size(72.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
                Text("Aún no tienes notas", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Pulsa el botón + para crear tu primera nota de estudio.",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(notas, key = { it.id }) { nota ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(shape = RoundedCornerShape(50), color = MaterialTheme.colorScheme.secondaryContainer) {
                                Text(
                                    vm.norma(nota.normaId)?.nombreCorto ?: "Norma",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                            Box(Modifier.weight(1f))
                            IconButton(onClick = { notaAEliminar = nota }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar nota", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                        Text(nota.texto, style = MaterialTheme.typography.bodyLarge)
                        Text(
                            formato.format(Date(nota.fecha)),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }

    notaAEliminar?.let { nota ->
        AlertDialog(
            onDismissRequest = { notaAEliminar = null },
            title = { Text("Eliminar nota") },
            text = { Text("Esta acción no se puede deshacer. ¿Deseas eliminar la nota?") },
            confirmButton = {
                TextButton(onClick = {
                    vm.eliminarNota(nota)
                    notaAEliminar = null
                }) { Text("Eliminar") }
            },
            dismissButton = { TextButton(onClick = { notaAEliminar = null }) { Text("Cancelar") } }
        )
    }
}
