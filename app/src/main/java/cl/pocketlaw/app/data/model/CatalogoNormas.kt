package cl.pocketlaw.app.data.model

data class Articulo(val numero: Int, val texto: String)

data class Modificacion(val anio: Int, val descripcion: String)

data class Material(val tipo: String, val titulo: String, val resumen: String)

data class Norma(
    val id: String,
    val nombre: String,
    val nombreCorto: String,
    val materia: String,
    val articulos: List<Articulo>,
    val modificaciones: List<Modificacion>,
    val material: List<Material>
)

object CatalogoNormas {

    private fun art(numero: Int, texto: String) = Articulo(numero, texto)
    private fun cambio(anio: Int, descripcion: String) = Modificacion(anio, descripcion)
    private fun principio(titulo: String, resumen: String) = Material("Principio", titulo, resumen)
    private fun doctrina(titulo: String, resumen: String) = Material("Doctrina", titulo, resumen)
    private fun fallo(titulo: String, resumen: String) = Material("Jurisprudencia", titulo, resumen)

    val normas: List<Norma> = listOf(
        Norma(
            id = "constitucion",
            nombre = "Constitución Política de la República",
            nombreCorto = "Constitución",
            materia = "Derecho Constitucional",
            articulos = listOf(
                art(1, "Las personas nacen libres e iguales en dignidad y derechos. La familia es el núcleo fundamental de la sociedad."),
                art(4, "Chile es una república democrática."),
                art(5, "La soberanía reside esencialmente en la Nación. Su ejercicio se realiza por el pueblo a través del plebiscito y de elecciones periódicas y, también, por las autoridades que la Constitución establece."),
                art(6, "Los órganos del Estado deben someter su acción a la Constitución y a las normas dictadas conforme a ella, y garantizar el orden institucional de la República."),
                art(7, "Los órganos del Estado actúan válidamente previa investidura regular de sus integrantes, dentro de su competencia y en la forma que prescriba la ley."),
                art(19, "La Constitución asegura a todas las personas, entre otros derechos: la vida y la integridad física y psíquica, la igualdad ante la ley y la igual protección de la ley en el ejercicio de sus derechos."),
                art(24, "El gobierno y la administración del Estado corresponden al Presidente de la República, quien es el Jefe del Estado.")
            ),
            modificaciones = listOf(
                cambio(2005, "Reforma constitucional que actualiza la estructura de los poderes del Estado."),
                cambio(2010, "Ajuste en materias de organización y reglas de funcionamiento."),
                cambio(2022, "Modificación de ejemplo sobre derechos y garantías.")
            ),
            material = listOf(
                principio("Supremacía constitucional", "Ningún acto de los órganos del Estado puede contradecir la Constitución."),
                principio("Legalidad de la competencia", "Las autoridades solo pueden actuar dentro de su competencia y en la forma que fija la ley."),
                doctrina("La dignidad como fundamento", "La doctrina entiende la dignidad humana como base de los derechos fundamentales y guía para interpretarlos."),
                fallo("Caso de ejemplo: igualdad ante la ley", "Caso ficticio en que se resuelve que una diferencia de trato requiere una justificación razonable.")
            )
        ),
        Norma(
            id = "codigo_civil",
            nombre = "Código Civil",
            nombreCorto = "Código Civil",
            materia = "Derecho Civil",
            articulos = listOf(
                art(1, "La ley es una declaración de la voluntad soberana que, manifestada en la forma prescrita por la Constitución, manda, prohíbe o permite."),
                art(2, "La costumbre no constituye derecho sino en los casos en que la ley se remite a ella."),
                art(4, "Las disposiciones contenidas en los Códigos de Comercio, de Minería, del Ejército y Armada, y demás especiales, se aplicarán con preferencia a las de este Código."),
                art(19, "Cuando el sentido de la ley es claro, no se desatenderá su tenor literal, a pretexto de consultar su espíritu."),
                art(1437, "Las obligaciones nacen del concurso real de las voluntades de dos o más personas, como en los contratos; de un hecho voluntario de la persona que se obliga; de un hecho que ha inferido injuria o daño a otra persona, como en los delitos y cuasidelitos; o por disposición de la ley."),
                art(1445, "Para que una persona se obligue a otra por un acto o declaración de voluntad es necesario que sea legalmente capaz, que consienta sin vicios, que recaiga sobre un objeto lícito y que tenga una causa lícita."),
                art(1545, "Todo contrato legalmente celebrado es una ley para los contratantes, y no puede ser invalidado sino por su consentimiento mutuo o por causas legales."),
                art(1546, "Los contratos deben ejecutarse de buena fe, y por consiguiente obligan no sólo a lo que en ellos se expresa, sino a todas las cosas que emanan de la naturaleza de la obligación, o que por la ley o la costumbre pertenecen a ella."),
                art(1698, "Incumbe probar las obligaciones o su extinción al que alega aquéllas o ésta."),
                art(2314, "El que ha cometido un delito o cuasidelito que ha inferido daño a otro es obligado a la indemnización, sin perjuicio de la pena que le impongan las leyes por el delito o cuasidelito.")
            ),
            modificaciones = listOf(
                cambio(1999, "Actualización de ejemplo en materia de filiación."),
                cambio(2013, "Modificación de ejemplo sobre regímenes patrimoniales."),
                cambio(2020, "Actualización de ejemplo en normas de capacidad.")
            ),
            material = listOf(
                principio("Autonomía de la voluntad", "Las personas pueden crear obligaciones y fijar su contenido dentro de los límites de la ley."),
                principio("Buena fe contractual", "Los contratos se ejecutan de buena fe y obligan a lo expresado y a lo que emana de su naturaleza."),
                doctrina("Responsabilidad extracontractual", "Se analizan el hecho, el daño, la relación causal y la culpa o el dolo del autor."),
                fallo("Caso de ejemplo: indemnización de perjuicios", "Caso ficticio en que se ordena indemnizar un daño probado y vinculado al hecho del demandado.")
            )
        ),
        Norma(
            id = "codigo_penal",
            nombre = "Código Penal",
            nombreCorto = "Código Penal",
            materia = "Derecho Penal",
            articulos = listOf(
                art(1, "Delito es toda acción u omisión voluntaria penada por la ley. Las acciones u omisiones penadas por la ley se reputan siempre voluntarias, a no ser que conste lo contrario."),
                art(2, "Las acciones u omisiones que, cometidas con dolo, constituirían delito, se llaman cuasidelitos si solo hay culpa en quien las ejecuta."),
                art(3, "Los delitos, atendida su gravedad, se dividen en crímenes, simples delitos y faltas, y se califican de tales según la pena que les está asignada en la escala general."),
                art(10, "Están exentos de responsabilidad criminal, entre otros, el que obra en defensa de su persona o derechos, siempre que concurran los requisitos legales, y el que obra violentado por una fuerza irresistible."),
                art(18, "Ningún delito se castiga con otra pena que la que le señale una ley promulgada con anterioridad a su perpetración. Si se promulga después una ley más favorable antes de la sentencia de término, deberá aplicarse esta última.")
            ),
            modificaciones = listOf(
                cambio(2005, "Actualización de ejemplo en delitos contra las personas."),
                cambio(2018, "Modificación de ejemplo sobre delitos económicos."),
                cambio(2023, "Actualización de ejemplo en penas y su aplicación.")
            ),
            material = listOf(
                principio("Legalidad penal", "No hay delito ni pena sin una ley previa que los establezca."),
                principio("Favorabilidad de la ley penal", "Si una ley posterior es más favorable al acusado, se aplica antes de la sentencia de término."),
                doctrina("Dolo y culpa", "El dolo exige conocimiento y voluntad del hecho; la culpa se produce por falta del cuidado debido."),
                fallo("Caso de ejemplo: legítima defensa", "Caso ficticio en que se analiza si la reacción fue proporcional a la agresión.")
            )
        ),
        Norma(
            id = "codigo_trabajo",
            nombre = "Código del Trabajo",
            nombreCorto = "Código del Trabajo",
            materia = "Derecho Laboral",
            articulos = listOf(
                art(1, "Las relaciones laborales entre los empleadores y los trabajadores se regularán por este Código y por sus leyes complementarias."),
                art(3, "Para todos los efectos legales se entiende por empleador a la persona natural o jurídica que utiliza los servicios intelectuales o materiales de una o más personas en virtud de un contrato de trabajo."),
                art(5, "Los derechos establecidos por las leyes laborales son irrenunciables, mientras subsista el contrato de trabajo."),
                art(7, "Contrato individual de trabajo es una convención por la cual el empleador y el trabajador se obligan recíprocamente, éste a prestar servicios personales bajo dependencia y subordinación del primero, y aquél a pagar por estos servicios una remuneración determinada."),
                art(8, "Toda prestación de servicios en los términos señalados en el artículo anterior hace presumir la existencia de un contrato de trabajo."),
                art(22, "La jornada ordinaria de trabajo tiene un máximo semanal fijado por la ley. Consulta la fuente oficial para conocer el límite vigente."),
                art(159, "El contrato de trabajo termina, entre otras causales, por mutuo acuerdo de las partes, renuncia del trabajador con aviso, muerte del trabajador, vencimiento del plazo, conclusión del servicio que dio origen al contrato y caso fortuito o fuerza mayor."),
                art(161, "El empleador puede poner término al contrato invocando las necesidades de la empresa, como las derivadas de su racionalización o modernización, bajas en la productividad o cambios en las condiciones del mercado.")
            ),
            modificaciones = listOf(
                cambio(2001, "Modificación de ejemplo sobre jornada de trabajo."),
                cambio(2019, "Actualización de ejemplo en normas de contratación."),
                cambio(2024, "Modificación de ejemplo sobre trabajo a distancia.")
            ),
            material = listOf(
                principio("Irrenunciabilidad de derechos", "Los derechos laborales no se pueden renunciar mientras dure el contrato."),
                principio("Primacía de la realidad", "Importa la forma real en que se prestan los servicios, más que el nombre que se dé al contrato."),
                doctrina("Elementos del contrato de trabajo", "Prestación personal de servicios, subordinación o dependencia y pago de una remuneración."),
                fallo("Caso de ejemplo: presunción de contrato", "Caso ficticio en que se declara una relación laboral por existir subordinación y pago periódico.")
            )
        ),
        Norma(
            id = "codigo_procesal_penal",
            nombre = "Código Procesal Penal",
            nombreCorto = "Cód. Procesal Penal",
            materia = "Derecho Procesal Penal",
            articulos = listOf(
                art(1, "Ninguna persona podrá ser condenada o penada sino en virtud de una sentencia fundada, dictada por un tribunal imparcial. Toda persona tiene derecho a un juicio previo, oral y público."),
                art(3, "El ministerio público dirige en forma exclusiva la investigación de los hechos constitutivos de delito, de los que determinen la participación punible y de los que acrediten la inocencia del imputado."),
                art(4, "Ninguna persona será considerada culpable ni tratada como tal en tanto no fuere condenada por una sentencia firme."),
                art(7, "Los derechos y garantías que la Constitución y las leyes reconocen al imputado pueden hacerse valer desde la primera actuación del procedimiento dirigido en su contra y hasta la completa ejecución de la sentencia."),
                art(8, "Todo imputado tiene derecho a ser defendido por un letrado desde los actos iniciales de la investigación y hasta la completa ejecución de la sentencia.")
            ),
            modificaciones = listOf(
                cambio(2005, "Actualización de ejemplo en etapas del procedimiento."),
                cambio(2016, "Modificación de ejemplo sobre medidas cautelares."),
                cambio(2022, "Actualización de ejemplo en derechos de las víctimas.")
            ),
            material = listOf(
                principio("Presunción de inocencia", "Nadie es tratado como culpable mientras no exista una sentencia firme."),
                principio("Juicio previo, oral y público", "La condena exige un juicio desarrollado ante un tribunal imparcial."),
                doctrina("Rol del Ministerio Público", "Dirige la investigación de manera exclusiva y debe también considerar los hechos que favorecen al imputado."),
                fallo("Caso de ejemplo: derecho a defensa", "Caso ficticio en que se anula una actuación realizada sin la presencia de un defensor.")
            )
        ),
        Norma(
            id = "ley_consumidor",
            nombre = "Ley N.º 19.496 sobre Protección de los Derechos de los Consumidores",
            nombreCorto = "Ley del Consumidor",
            materia = "Derecho del Consumidor",
            articulos = listOf(
                art(1, "La ley regula las relaciones entre proveedores y consumidores, establece las infracciones y el procedimiento aplicable en materia de defensa del consumidor."),
                art(3, "Son derechos básicos del consumidor, entre otros, la información veraz y oportuna, la libre elección del bien o servicio, la seguridad en el consumo y la reparación e indemnización adecuada."),
                art(12, "Todo proveedor de bienes o servicios está obligado a respetar los términos, condiciones y modalidades conforme a los cuales se hubiere ofrecido o convenido con el consumidor la entrega del bien o la prestación del servicio."),
                art(20, "En caso de productos con deficiencias, el consumidor puede optar entre la reparación gratuita, la reposición o la devolución de la cantidad pagada, según las condiciones que fija la ley.")
            ),
            modificaciones = listOf(
                cambio(2004, "Actualización de ejemplo en procedimiento y sanciones."),
                cambio(2018, "Modificación de ejemplo sobre derechos de los consumidores."),
                cambio(2021, "Actualización de ejemplo en comercio electrónico.")
            ),
            material = listOf(
                principio("Protección del consumidor", "La ley busca equilibrar la relación entre el proveedor y el consumidor."),
                principio("Cumplimiento de lo ofrecido", "El proveedor debe respetar las condiciones que ofreció o acordó."),
                doctrina("Asimetría de información", "El proveedor conoce más del producto, por lo que la ley exige información clara y oportuna."),
                fallo("Caso de ejemplo: garantía por producto defectuoso", "Caso ficticio en que se ordena la devolución del precio de un producto con falla de fábrica.")
            )
        )
    )

    fun porId(id: String): Norma? = normas.firstOrNull { it.id == id }

    fun materias(): List<String> = normas.map { it.materia }.distinct()

    fun modificacionesRecientes(cantidad: Int): List<Pair<Norma, Modificacion>> =
        normas.flatMap { norma -> norma.modificaciones.map { norma to it } }
            .sortedByDescending { it.second.anio }
            .take(cantidad)
}
