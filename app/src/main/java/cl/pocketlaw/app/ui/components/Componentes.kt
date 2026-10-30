package cl.pocketlaw.app.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cl.pocketlaw.app.R
import cl.pocketlaw.app.ui.theme.Exito
import cl.pocketlaw.app.ui.theme.Marino
import cl.pocketlaw.app.ui.theme.MarinoClaro

@Composable
fun CampoTexto(
    valor: String,
    alCambiar: (String) -> Unit,
    etiqueta: String,
    icono: ImageVector,
    error: String?,
    modifier: Modifier = Modifier,
    teclado: KeyboardType = KeyboardType.Text,
    esClave: Boolean = false,
    lineas: Int = 1,
    accion: ImeAction = ImeAction.Next
) {
    var claveVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = valor,
        onValueChange = alCambiar,
        label = { Text(etiqueta) },
        leadingIcon = { Icon(icono, contentDescription = null) },
        trailingIcon = when {
            esClave -> ({
                IconButton(onClick = { claveVisible = !claveVisible }) {
                    Icon(
                        if (claveVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "Mostrar u ocultar contraseña"
                    )
                }
            })
            error != null -> ({
                Icon(Icons.Default.Warning, contentDescription = "Campo con error", tint = MaterialTheme.colorScheme.error)
            })
            valor.isNotEmpty() && lineas == 1 -> ({
                Icon(Icons.Default.CheckCircle, contentDescription = "Campo válido", tint = Exito)
            })
            else -> null
        },
        isError = error != null,
        supportingText = if (error != null) ({ Text(error) }) else null,
        singleLine = lineas == 1,
        minLines = lineas,
        visualTransformation = if (esClave && !claveVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (esClave) KeyboardType.Password else teclado,
            imeAction = accion
        ),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun EncabezadoMarca(titulo: String, subtitulo: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(Marino, MarinoClaro)))
            .statusBarsPadding()
            .padding(vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_logo),
            contentDescription = "Logo de PocketLaw",
            modifier = Modifier.size(88.dp)
        )
        Spacer(Modifier.height(10.dp))
        Text(titulo, style = MaterialTheme.typography.headlineMedium, color = Color.White)
        Text(subtitulo, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.85f))
    }
}
