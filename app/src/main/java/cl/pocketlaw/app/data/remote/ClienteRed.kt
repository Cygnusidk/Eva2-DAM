package cl.pocketlaw.app.data.remote

import cl.pocketlaw.app.BuildConfig
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ClienteRed {

    private val http: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    private fun crearRetrofit(urlBase: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(urlBase)
            .client(http)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val usuariosApi: UsuariosApi by lazy { crearRetrofit(BuildConfig.URL_USUARIOS).create(UsuariosApi::class.java) }

    val estudioApi: EstudioApi by lazy { crearRetrofit(BuildConfig.URL_ESTUDIO).create(EstudioApi::class.java) }

    val indicadoresApi: IndicadoresApi by lazy { crearRetrofit("https://mindicador.cl/").create(IndicadoresApi::class.java) }
}
