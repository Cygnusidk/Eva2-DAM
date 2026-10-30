@file:OptIn(ExperimentalMaterial3Api::class)

package cl.pocketlaw.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.pocketlaw.app.ui.components.DialogoNota
import cl.pocketlaw.app.ui.components.rememberFabrica
import cl.pocketlaw.app.ui.theme.Dorado
import cl.pocketlaw.app.ui.theme.Marino
import cl.pocketlaw.app.viewmodel.EstudioViewModel

@Composable
fun PantallaDetalle(normaId: String, volver: () -> Unit, vm: EstudioViewModel = viewModel(factory = rememberFabrica())) {
    val norma = vm.norma(normaId)
    var mostrarDialogo by remember { mutableStateOf(false) }
    var historialVisible by remember { mutableStateOf(false) }
    LaunchedEffect(normaId) { vm.filtrarAnio(null) }
    val flujoDestacados = remember(normaId) { vm.destacadosDe(normaId) }
    val destacados by flujoDestacados.collectAsStateWithLifecycle(initialValue = emptySet<Int>())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(norma?.nombreCorto ?: "Norma", fontFamily = FontFamily.Serif) },
                navigationIcon = {
                    IconButton(onClick = volver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Marino,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            if (norma != null) {
                ExtendedFloatingActionButton(
                    onClick = { mostrarDialogo = true },
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text("Nueva nota") }
                )
            }
        }
    ) { relleno ->
        if (norma == null) {
            Text("La norma solicitada no existe.", modifier = Modifier.padding(relleno).padding(16.dp))
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = relleno.calculateTopPadding() + 16.dp,
                bottom = relleno.calculateBottomPadding() + 96.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(norma.nombre, style = MaterialTheme.typography.headlineSmall)
                    Text(norma.materia, color = MaterialTheme.colorScheme.secondary)
                }
            }
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.TextFields, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Tamaño de letra: ${vm.tamanoLetra.toInt()} pt")
                        }
                        Slider(
                            value = vm.tamanoLetra,
                            onValueChange = { vm.tamanoLetra = it },
                            onValueChangeFinished = { vm.guardarTamanoLetra() },
                            valueRange = 12f..28f
                        )
                    }
                }
            }
            items(norma.articulos, key = { it.numero }) { articulo ->
                val marcado = articulo.numero in destacados
                val colorFondo by animateColorAsState(
                    targetValue = if (marcado) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface,
                    label = "colorArticulo"
                )
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colorFondo),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "Artículo ${articulo.numero}",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { vm.alternarDestacado(norma.id, articulo.numero, !marcado) }) {
                                Icon(
                                    imageVector = if (marcado) Icons.Default.Star else Icons.Default.StarBorder,
                                    contentDescription = if (marcado) "Quitar destacado" else "Destacar artículo",
                                    tint = if (marcado) Dorado else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Text(
                            articulo.texto,
                            fontFamily = FontFamily.Serif,
                            fontSize = vm.tamanoLetra.sp,
                            lineHeight = (vm.tamanoLetra * 1.5f).sp
                        )
                    }
                }
            }
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp),
                    onClick = { historialVisible = !historialVisible },
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.History, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                            Spacer(Modifier.width(8.dp))
                            Text(
                                "Historial de modificaciones (datos de prueba)",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                if (historialVisible) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = if (historialVisible) "Ocultar historial" else "Mostrar historial"
                            )
                        }
                        AnimatedVisibility(visible = historialVisible) {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    item {
                                        FilterChip(
                                            selected = vm.anioFiltro == null,
                                            onClick = { vm.filtrarAnio(null) },
                                            label = { Text("Todos") }
                                        )
                                    }
                                    items(norma.modificaciones.map { it.anio }.sortedDescending(), key = { it }) { anio ->
                                        FilterChip(
                                            selected = vm.anioFiltro == anio,
                                            onClick = { vm.filtrarAnio(anio) },
                                            label = { Text(anio.toString()) }
                                        )
                                    }
                                }
                                norma.modificaciones
                                    .filter { vm.anioFiltro == null || it.anio == vm.anioFiltro }
                                    .sortedByDescending { it.anio }
                                    .forEach { cambio ->
                                    Row(verticalAlignment = Alignment.Top) {
                                        Surface(
                                            shape = RoundedCornerShape(50),
                                            color = MaterialTheme.colorScheme.primary
                                        ) {
                                            Text(
                                                cambio.anio.toString(),
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                                                style = MaterialTheme.typography.labelMedium,
                                                color = MaterialTheme.colorScheme.onPrimary
                                            )
                                        }
                                        Spacer(Modifier.width(10.dp))
                                        Text(cambio.descripcion, style = MaterialTheme.typography.bodyMedium)
                                    }
                                }
                            }
                        }
                    }
                }
            }
            item {
                Text("Material de estudio", style = MaterialTheme.typography.titleLarge)
            }
            items(norma.material, key = { it.titulo }) { material ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Surface(shape = RoundedCornerShape(50), color = MaterialTheme.colorScheme.primaryContainer) {
                            Text(
                                material.tipo,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        Text(material.titulo, style = MaterialTheme.typography.titleMedium)
                        Text(material.resumen, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            item {
                Text(
                    "Contenido de ejemplo con fines académicos. No reemplaza las fuentes oficiales de legislación ni constituye asesoría jurídica.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    if (mostrarDialogo) {
        DialogoNota(normaFija = normaId, vm = vm, cerrar = { mostrarDialogo = false })
    }
}
