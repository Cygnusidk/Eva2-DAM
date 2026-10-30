package cl.pocketlaw.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cl.pocketlaw.app.ui.navigation.AppNavegacion
import cl.pocketlaw.app.ui.theme.TemaPocketLaw

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT))
        setContent {
            TemaPocketLaw {
                AppNavegacion()
            }
        }
    }
}
