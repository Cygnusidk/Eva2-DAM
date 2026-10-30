package cl.pocketlaw.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Marino = Color(0xFF1B2A41)
val MarinoClaro = Color(0xFF324A6D)
val Dorado = Color(0xFFC9A227)
val Exito = Color(0xFF2E7D32)

private val EsquemaClaro = lightColorScheme(
    primary = Marino,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDCE3EE),
    onPrimaryContainer = Color(0xFF0F1A2B),
    secondary = Color(0xFF8A6D12),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF6EBC4),
    onSecondaryContainer = Color(0xFF3B2F05),
    background = Color(0xFFF7F4EE),
    onBackground = Color(0xFF1C1B1F),
    surface = Color.White,
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFECE8DF),
    onSurfaceVariant = Color(0xFF4A4740),
    error = Color(0xFFB3261E),
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B)
)

private val EsquemaOscuro = darkColorScheme(
    primary = Color(0xFFB8C7E0),
    onPrimary = Marino,
    primaryContainer = Color(0xFF2A3B57),
    onPrimaryContainer = Color(0xFFDCE3EE),
    secondary = Color(0xFFE2C46B),
    onSecondary = Color(0xFF3B2F05),
    secondaryContainer = Color(0xFF4A3D10),
    onSecondaryContainer = Color(0xFFF6EBC4),
    background = Color(0xFF121820),
    onBackground = Color(0xFFE6E1E5),
    surface = Color(0xFF1B2230),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF2A3140),
    onSurfaceVariant = Color(0xFFC9C5CA),
    error = Color(0xFFF2B8B5),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC)
)

private val Tipografia = Typography(
    headlineMedium = TextStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 30.sp),
    headlineSmall = TextStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 24.sp),
    titleLarge = TextStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold, fontSize = 22.sp),
    titleMedium = TextStyle(fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
)

@Composable
fun TemaPocketLaw(contenido: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) EsquemaOscuro else EsquemaClaro,
        typography = Tipografia,
        content = contenido
    )
}
