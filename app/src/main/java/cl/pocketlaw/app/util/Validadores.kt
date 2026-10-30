package cl.pocketlaw.app.util

object Validadores {

    private val patronCorreo = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun nombre(valor: String): String? = when {
        valor.isBlank() -> "El nombre es obligatorio"
        valor.trim().length < 3 -> "Debe tener al menos 3 caracteres"
        !valor.all { it.isLetter() || it == ' ' } -> "Solo se permiten letras y espacios"
        else -> null
    }

    fun correo(valor: String): String? = when {
        valor.isBlank() -> "El correo es obligatorio"
        !patronCorreo.matches(valor.trim()) -> "Ingresa un correo válido"
        else -> null
    }

    fun clave(valor: String): String? = when {
        valor.isBlank() -> "La contraseña es obligatoria"
        valor.length < 8 -> "Debe tener al menos 8 caracteres"
        valor.none { it.isDigit() } -> "Debe incluir al menos un número"
        valor.none { it.isUpperCase() } -> "Debe incluir una letra mayúscula"
        else -> null
    }

    fun claveIngreso(valor: String): String? =
        if (valor.isBlank()) "La contraseña es obligatoria" else null

    fun confirmacion(clave: String, confirmacion: String): String? = when {
        confirmacion.isBlank() -> "Confirma tu contraseña"
        clave != confirmacion -> "Las contraseñas no coinciden"
        else -> null
    }

    fun nota(valor: String): String? = when {
        valor.isBlank() -> "Escribe el contenido de la nota"
        valor.trim().length < 5 -> "Debe tener al menos 5 caracteres"
        valor.length > 300 -> "Máximo 300 caracteres"
        else -> null
    }
}
