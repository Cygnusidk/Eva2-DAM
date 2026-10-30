package cl.pocketlaw.app.data.repository

import cl.pocketlaw.app.data.model.Modificacion
import cl.pocketlaw.app.data.model.Norma

interface LegislacionRepository {
    fun normas(): List<Norma>
    fun norma(id: String): Norma?
    fun materias(): List<String>
    fun modificacionesRecientes(cantidad: Int): List<Pair<Norma, Modificacion>>
}
