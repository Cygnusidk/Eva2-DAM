package cl.pocketlaw.app.data.model

data class Nota(
    val id: Long,
    val usuarioId: Long,
    val normaId: String,
    val texto: String,
    val fecha: Long
)

data class Indicadores(
    val uf: Double,
    val utm: Double,
    val dolar: Double,
    val fecha: String
)
