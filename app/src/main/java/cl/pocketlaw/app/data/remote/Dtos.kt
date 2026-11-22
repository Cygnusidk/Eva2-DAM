package cl.pocketlaw.app.data.remote

data class RegistroDto(val nombre: String, val correo: String, val clave: String)

data class LoginDto(val correo: String, val clave: String)

data class ActualizarUsuarioDto(val nombre: String, val tamanoLetra: Float)

data class UsuarioDto(val id: Long, val nombre: String, val correo: String, val tamanoLetra: Float)

data class NotaNuevaDto(val usuarioId: Long, val normaId: String, val texto: String)

data class NotaTextoDto(val texto: String)

data class DestacadoNuevoDto(val usuarioId: Long, val normaId: String, val articulo: Int)

data class DestacadoDto(val id: Long, val usuarioId: Long, val normaId: String, val articulo: Int)

data class IndicadorDto(val valor: Double?, val fecha: String?)

data class IndicadoresDto(
    val fecha: String?,
    val uf: IndicadorDto?,
    val utm: IndicadorDto?,
    val dolar: IndicadorDto?
)
