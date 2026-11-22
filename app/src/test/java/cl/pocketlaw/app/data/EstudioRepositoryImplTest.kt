package cl.pocketlaw.app.data

import cl.pocketlaw.app.data.remote.EstudioApi
import cl.pocketlaw.app.data.repository.EstudioRepositoryImpl
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EstudioRepositoryImplTest {
    private lateinit var servidor: MockWebServer
    private lateinit var repositorio: EstudioRepositoryImpl

    private val jsonNota =
        """{"id":3,"usuarioId":1,"normaId":"codigo_civil","texto":"Repasar el artículo 1","fecha":1700000000000}"""

    @Before
    fun preparar() {
        servidor = MockWebServer()
        servidor.start()
        val api = Retrofit.Builder()
            .baseUrl(servidor.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EstudioApi::class.java)
        repositorio = EstudioRepositoryImpl(api)
    }

    @After
    fun limpiar() {
        servidor.shutdown()
    }

    @Test
    fun `listar las notas hace un GET y convierte el JSON`() = runTest {
        println("INICIO: comprobar la consulta de notas.")
        servidor.enqueue(MockResponse().setResponseCode(200).setBody("[$jsonNota]"))

        val resultado = repositorio.notas(1)

        assertEquals("Repasar el artículo 1", resultado.getOrThrow().first().texto)
        val solicitud = servidor.takeRequest()
        assertEquals("GET", solicitud.method)
        assertEquals("/api/notas?usuarioId=1", solicitud.path)
        println("CORRECTO: la nota llego convertida desde el JSON.")
    }

    @Test
    fun `crear una nota hace un POST con los datos`() = runTest {
        println("INICIO: comprobar la creacion de una nota.")
        // Preparación: el servidor responde que la nota fue creada.
        servidor.enqueue(MockResponse().setResponseCode(201).setBody(jsonNota))

        val resultado = repositorio.crearNota(1, "codigo_civil", "Repasar el artículo 1")

        assertTrue(resultado.isSuccess)
        val solicitud = servidor.takeRequest()
        assertEquals("POST", solicitud.method)
        assertEquals("/api/notas", solicitud.path)
        assertTrue(solicitud.body.readUtf8().contains("\"normaId\":\"codigo_civil\""))
        println("CORRECTO: el POST llevo los datos de la nota.")
    }

    @Test
    fun `si el servidor responde 500 el resultado es un error HTTP`() = runTest {
        println("INICIO: comprobar la respuesta con error 500.")
        servidor.enqueue(MockResponse().setResponseCode(500))

        val resultado = repositorio.notas(1)

        val error = resultado.exceptionOrNull()
        assertTrue(error is HttpException)
        assertEquals(500, (error as HttpException).code())
        println("CORRECTO: el error 500 llego como HttpException.")
    }
}
