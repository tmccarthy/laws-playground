package au.id.tmm.lawsplayground.semigroups

object Graphing {

  def dotSnippetFor(instance: Instance[_], typeClasses: Set[TypeClass], resultsPerTypeClass: TypeClass => LawTestResult): String = {
    dotSnippetFor(
      graphName = instance.name.replaceAll("""\W""", ""),
      typeClasses,
      colouring = t => resultsPerTypeClass(t) match {
        case LawTestResult.Pass => Some("green") // TODO this is not really accessible
        case LawTestResult.Fail => Some("red") // TODO this is not really accessible
      }
    )
  }

  private def dotSnippetFor(graphName: String, typeClasses: Set[TypeClass], colouring: TypeClass => Option[String]): String = {
    val nodesSnippet =
      typeClasses
        .map { t =>
          colouring(t) match {
            case Some(colour) => s"${t.name} [color=$colour]"
            case None         => t.name
          }
        }
        .mkString("\n")

    val links =
      for {
        typeClass <- typeClasses
        parent    <- typeClass.parents
      } yield parent -> typeClass

    val linksSnippet =
      links
        .map {
          case (parent, child) => {
            val lawNamesForEdge = child.laws.map(_.name).toList.mkString("\n")

            s"""${parent.name} -> ${child.name} [ label = "$lawNamesForEdge" ]"""
          }
        }
        .mkString("\n")

    s"""digraph $graphName {
       |  $nodesSnippet
       |
       |  $linksSnippet
       |}
       |""".stripMargin
  }

}
