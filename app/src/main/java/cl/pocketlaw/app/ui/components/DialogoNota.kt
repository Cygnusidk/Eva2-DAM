@file:OptIn(ExperimentalMaterial3Api::class)

package cl.pocketlaw.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cl.pocketlaw.app.viewmodel.EstudioViewModel

@Composable
fun DialogoNota(normaFija: String?, vm: EstudioViewModel, cerrar: () -> Unit) {
    var normaElegida by remember { mutableStateOf(normaFija ?: vm.catalogo.first().id) }

    LaunchedEffect(Unit) { vm.limpiarNota() }

    AlertDialog(
        onDismissRequest = cerrar,
        title = { Text("Nueva nota") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (normaFija == null) {
                    Text("Norma asociada")
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(vm.catalogo, key = { it.id }) { norma ->
                            FilterChip(
                                selected = normaElegida == norma.id,
                                onClick = { normaElegida = norma.id },
                                label = { Text(norma.nombreCorto) }
                            )
                        }
                    }
                } else {
                    Text("Norma: ${vm.norma(normaFija)?.nombre ?: ""}")
                }
                Spacer(Modifier.height(4.dp))
                CampoTexto(
                    valor = vm.textoNota,
                    alCambiar = vm::cambiarTextoNota,
                    etiqueta = "Contenido de la nota",
                    icono = Icons.Default.EditNote,
                    error = vm.errorNota,
                    lineas = 4
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { vm.guardarNota(normaElegida, cerrar) }) { Text("Guardar") }
        },
        dismissButton = {
            TextButton(onClick = cerrar) { Text("Cancelar") }
        }
    )
}
