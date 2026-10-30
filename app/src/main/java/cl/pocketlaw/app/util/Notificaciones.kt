package cl.pocketlaw.app.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import cl.pocketlaw.app.MainActivity
import cl.pocketlaw.app.R

object Notificaciones {

    private const val CANAL = "avisos_normativos"
    private const val ID_AVISO = 1001

    fun tienePermiso(context: Context): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            RecursosNativos.tienePermiso(context, Manifest.permission.POST_NOTIFICATIONS)

    @SuppressLint("MissingPermission")
    fun mostrarAviso(context: Context, titulo: String, texto: String) {
        val administrador = NotificationManagerCompat.from(context)
        administrador.createNotificationChannel(
            NotificationChannelCompat.Builder(CANAL, NotificationManagerCompat.IMPORTANCE_DEFAULT)
                .setName("Avisos de modificaciones")
                .build()
        )
        val intencion = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendiente = PendingIntent.getActivity(
            context, 0, intencion, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificacion = NotificationCompat.Builder(context, CANAL)
            .setSmallIcon(R.drawable.ic_notificacion)
            .setContentTitle(titulo)
            .setContentText(texto)
            .setStyle(NotificationCompat.BigTextStyle().bigText(texto))
            .setContentIntent(pendiente)
            .setAutoCancel(true)
            .build()
        try {
            administrador.notify(ID_AVISO, notificacion)
        } catch (e: SecurityException) {
            return
        }
    }
}
