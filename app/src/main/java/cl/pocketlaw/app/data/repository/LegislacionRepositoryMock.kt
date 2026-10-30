package cl.pocketlaw.app.data.repository

import cl.pocketlaw.app.data.model.CatalogoNormas
import cl.pocketlaw.app.data.model.Modificacion
import cl.pocketlaw.app.data.model.Norma

class LegislacionRepositoryMock : LegislacionRepository {
    override fun normas(): List<Norma> = CatalogoNormas.normas
    override fun norma(id: String): Norma? = CatalogoNormas.porId(id)
    override fun materias(): List<String> = CatalogoNormas.materias()
    override fun modificacionesRecientes(cantidad: Int): List<Pair<Norma, Modificacion>> =
        CatalogoNormas.modificacionesRecientes(cantidad)
}
