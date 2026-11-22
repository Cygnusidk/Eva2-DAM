package cl.pocketlaw.app.data.repository

import cl.pocketlaw.app.data.model.Indicadores

interface IndicadoresRepository {
    suspend fun obtener(): Result<Indicadores>
}
