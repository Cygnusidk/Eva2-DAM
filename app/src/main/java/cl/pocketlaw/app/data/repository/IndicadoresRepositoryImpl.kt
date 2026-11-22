package cl.pocketlaw.app.data.repository

import cl.pocketlaw.app.data.model.Indicadores
import cl.pocketlaw.app.data.remote.IndicadoresApi
import cl.pocketlaw.app.data.remote.llamarApi
import java.io.IOException

class IndicadoresRepositoryImpl(private val api: IndicadoresApi) : IndicadoresRepository {

    override suspend fun obtener(): Result<Indicadores> = llamarApi {
        val respuesta = api.indicadores()
        val uf = respuesta.uf?.valor
        val utm = respuesta.utm?.valor
        val dolar = respuesta.dolar?.valor
        if (uf == null || utm == null || dolar == null) throw IOException("Respuesta incompleta")
        Indicadores(uf = uf, utm = utm, dolar = dolar, fecha = respuesta.fecha?.take(10) ?: "")
    }
}
