package cl.pocketlaw.app.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

object RecursosNativos {

    private const val TIEMPO_MAXIMO_MS = 15000L

    fun tienePermiso(context: Context, permiso: String): Boolean =
        ContextCompat.checkSelfPermission(context, permiso) == PackageManager.PERMISSION_GRANTED

    fun tienePermisoUbicacion(context: Context): Boolean =
        tienePermiso(context, Manifest.permission.ACCESS_FINE_LOCATION) ||
            tienePermiso(context, Manifest.permission.ACCESS_COARSE_LOCATION)

    fun nuevaUriFoto(context: Context): Uri {
        val carpeta = File(context.filesDir, "fotos").apply { mkdirs() }
        val archivo = File(carpeta, "perfil_${System.currentTimeMillis()}.jpg")
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", archivo)
    }

    @SuppressLint("MissingPermission")
    fun obtenerUbicacion(context: Context, alResultado: (Location?) -> Unit) {
        val administrador = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val precisa = tienePermiso(context, Manifest.permission.ACCESS_FINE_LOCATION)
        val proveedor = when {
            precisa && administrador.isProviderEnabled(LocationManager.GPS_PROVIDER) -> LocationManager.GPS_PROVIDER
            administrador.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> LocationManager.NETWORK_PROVIDER
            else -> null
        }
        if (proveedor == null) {
            alResultado(null)
            return
        }

        try {
            val ultima = administrador.getLastKnownLocation(proveedor)
            if (ultima != null) {
                alResultado(ultima)
                return
            }

            val manejador = Handler(Looper.getMainLooper())
            var terminado = false

            val escucha = object : LocationListener {
                override fun onLocationChanged(ubicacion: Location) {
                    if (terminado) return
                    terminado = true
                    administrador.removeUpdates(this)
                    manejador.removeCallbacksAndMessages(null)
                    alResultado(ubicacion)
                }

                override fun onProviderEnabled(nombre: String) {}

                override fun onProviderDisabled(nombre: String) {}

                override fun onStatusChanged(nombre: String?, estado: Int, extras: Bundle?) {}
            }

            administrador.requestLocationUpdates(proveedor, 0L, 0f, escucha, Looper.getMainLooper())

            manejador.postDelayed({
                if (!terminado) {
                    terminado = true
                    administrador.removeUpdates(escucha)
                    alResultado(null)
                }
            }, TIEMPO_MAXIMO_MS)
        } catch (e: SecurityException) {
            alResultado(null)
        }
    }
}
