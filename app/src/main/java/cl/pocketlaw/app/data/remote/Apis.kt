package cl.pocketlaw.app.data.remote

import cl.pocketlaw.app.data.model.Nota
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UsuariosApi {
    @POST("api/usuarios/registro")
    suspend fun registrar(@Body datos: RegistroDto): UsuarioDto

    @POST("api/usuarios/login")
    suspend fun iniciarSesion(@Body datos: LoginDto): UsuarioDto

    @GET("api/usuarios/{id}")
    suspend fun obtener(@Path("id") id: Long): UsuarioDto

    @PUT("api/usuarios/{id}")
    suspend fun actualizar(@Path("id") id: Long, @Body datos: ActualizarUsuarioDto): UsuarioDto
}

interface EstudioApi {
    @GET("api/notas")
    suspend fun notas(@Query("usuarioId") usuarioId: Long): List<Nota>

    @POST("api/notas")
    suspend fun crearNota(@Body datos: NotaNuevaDto): Nota

    @PUT("api/notas/{id}")
    suspend fun editarNota(@Path("id") id: Long, @Body datos: NotaTextoDto): Nota

    @DELETE("api/notas/{id}")
    suspend fun eliminarNota(@Path("id") id: Long): Response<Unit>

    @GET("api/destacados")
    suspend fun destacados(
        @Query("usuarioId") usuarioId: Long,
        @Query("normaId") normaId: String
    ): List<DestacadoDto>

    @POST("api/destacados")
    suspend fun marcarDestacado(@Body datos: DestacadoNuevoDto): DestacadoDto

    @DELETE("api/destacados")
    suspend fun desmarcarDestacado(
        @Query("usuarioId") usuarioId: Long,
        @Query("normaId") normaId: String,
        @Query("articulo") articulo: Int
    ): Response<Unit>
}

interface IndicadoresApi {
    @GET("api")
    suspend fun indicadores(): IndicadoresDto
}
