@file:OptIn(ExperimentalMaterial3Api::class)

package cl.pocketlaw.app.ui.screens

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cl.pocketlaw.app.util.RecursosNativos
import cl.pocketlaw.app.viewmodel.PerfilViewModel
import coil.compose.AsyncImage

@Composable
fun PantallaPerfil(vm: PerfilViewModel, alSalir: () -> Unit) {
    val contexto = LocalContext.current
    val usuario by vm.usuario.collectAsStateWithLifecycle()
    var uriFoto by remember { mutableStateOf<Uri?>(null) }

    val camara = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { exito ->
        val uri = uriFoto
        if (exito && uri != null) vm.guardarFoto(uri.toString()) else vm.informar("No se guardó la foto.")
    }

    fun abrirCamara() {
        val uri = RecursosNativos.nuevaUriFoto(contexto)
        uriFoto = uri
        camara.launch(uri)
    }

    val permisoCamara = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { concedido ->
        if (concedido) abrirCamara()
        else vm.informar("Permiso de cámara denegado. Puedes habilitarlo desde los ajustes del dispositivo.")
    }

    fun buscarUbicacion() {
        vm.iniciarBusquedaUbicacion()
        RecursosNativos.obtenerUbicacion(contexto) { vm.resultadoUbicacion(it) }
    }

    val permisoUbicacion = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { resultado ->
        if (resultado.values.any { it }) buscarUbicacion()
        else vm.informar("Permiso de ubicación denegado. Puedes habilitarlo desde los ajustes del dispositivo.")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            val foto = usuario?.fotoUri
            if (foto != null) {
                AsyncImage(
                    model = Uri.parse(foto),
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(Icons.Default.Person, contentDescription = "Sin foto de perfil", modifier = Modifier.size(64.dp))
            }
        }
        Text(usuario?.nombre ?: "", style = MaterialTheme.typography.headlineSmall)
        Text(usuario?.correo ?: "", color = MaterialTheme.colorScheme.onSurfaceVariant)

        AnimatedVisibility(visible = vm.mensaje != null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                    Spacer(Modifier.width(10.dp))
                    Text(vm.mensaje ?: "", color = MaterialTheme.colorScheme.onErrorContainer)
                }
            }
        }

        Button(
            onClick = {
                if (RecursosNativos.tienePermiso(contexto, Manifest.permission.CAMERA)) abrirCamara()
                else permisoCamara.launch(Manifest.permission.CAMERA)
            },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Icon(Icons.Default.CameraAlt, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Tomar foto de perfil")
        }

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                    Spacer(Modifier.width(8.dp))
                    Text("Mi ubicación", style = MaterialTheme.typography.titleMedium)
                }
                val coordenadas = vm.coordenadas
                if (coordenadas != null) {
                    Text("Latitud: ${"%.5f".format(coordenadas.first)}")
                    Text("Longitud: ${"%.5f".format(coordenadas.second)}")
                    OutlinedButton(
                        onClick = {
                            val destino = Uri.parse(
                                "geo:0,0?q=${coordenadas.first},${coordenadas.second}(${Uri.encode("Mi ubicación")})"
                            )
                            try {
                                contexto.startActivity(Intent(Intent.ACTION_VIEW, destino))
                            } catch (e: ActivityNotFoundException) {
                                vm.informar("No hay una aplicación de mapas instalada.")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Map, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Ver en el mapa")
                    }
                } else {
                    Text(
                        "Obtén tu ubicación para encontrar bibliotecas y tribunales cercanos.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Button(
                    onClick = {
                        if (RecursosNativos.tienePermisoUbicacion(contexto)) buscarUbicacion()
                        else permisoUbicacion.launch(
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                        )
                    },
                    enabled = !vm.buscandoUbicacion,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (vm.buscandoUbicacion) {
                        CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text("Obtener mi ubicación")
                    }
                }
            }
        }

        OutlinedButton(
            onClick = { vm.cerrarSesion(alSalir) },
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Cerrar sesión")
        }
    }
}
