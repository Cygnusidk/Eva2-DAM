package cl.pocketlaw.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cl.pocketlaw.app.ui.components.CampoTexto
import cl.pocketlaw.app.ui.components.EncabezadoMarca
import cl.pocketlaw.app.viewmodel.AuthViewModel

@Composable
fun PantallaRegistro(vm: AuthViewModel, alRegistrar: () -> Unit, volver: () -> Unit) {
    val form = vm.form

    LaunchedEffect(Unit) { vm.limpiar() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .imePadding()
            .verticalScroll(rememberScrollState())
    ) {
        EncabezadoMarca("Crear cuenta", "Guarda tus notas y destacados")

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CampoTexto(
                    valor = form.nombre,
                    alCambiar = vm::cambiarNombre,
                    etiqueta = "Nombre completo",
                    icono = Icons.Default.Person,
                    error = form.errorNombre
                )
                CampoTexto(
                    valor = form.correo,
                    alCambiar = vm::cambiarCorreo,
                    etiqueta = "Correo electrónico",
                    icono = Icons.Default.Email,
                    error = form.errorCorreo,
                    teclado = KeyboardType.Email
                )
                CampoTexto(
                    valor = form.clave,
                    alCambiar = vm::cambiarClave,
                    etiqueta = "Contraseña",
                    icono = Icons.Default.Lock,
                    error = form.errorClave,
                    esClave = true
                )
                CampoTexto(
                    valor = form.confirmacion,
                    alCambiar = vm::cambiarConfirmacion,
                    etiqueta = "Confirmar contraseña",
                    icono = Icons.Default.Lock,
                    error = form.errorConfirmacion,
                    esClave = true,
                    accion = ImeAction.Done
                )

                Spacer(Modifier.height(4.dp))
                Button(
                    onClick = { vm.registrar(alRegistrar) },
                    enabled = !form.cargando,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    if (form.cargando) {
                        CircularProgressIndicator(Modifier.size(22.dp), strokeWidth = 2.dp, color = MaterialTheme.colorScheme.onPrimary)
                    } else {
                        Text("Crear cuenta")
                    }
                }
                TextButton(onClick = volver, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Text("¿Ya tienes cuenta? Inicia sesión")
                }
            }
        }
        Spacer(Modifier.navigationBarsPadding())
    }
}
