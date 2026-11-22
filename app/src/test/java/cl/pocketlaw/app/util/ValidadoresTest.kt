package cl.pocketlaw.app.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ValidadoresTest {

    @Test
    fun `un correo sin arroba devuelve error`() {
        println("INICIO: comprobar la validacion del correo.")

        val error = Validadores.correo("pruebapocketlaw.cl")

        assertEquals("Ingresa un correo válido", error)
        println("CORRECTO: el correo incorrecto fue rechazado.")
    }

    @Test
    fun `una contraseña con mayuscula, numero y 8 caracteres es valida`() {
        println("INICIO: comprobar la validacion de la contraseña.")

        val error = Validadores.clave("Prueba123")

        assertNull(error)
        println("CORRECTO: la contraseña fue aceptada.")
    }
}
