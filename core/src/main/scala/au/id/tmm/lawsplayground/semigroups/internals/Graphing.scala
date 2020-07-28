package au.id.tmm.lawsplayground.semigroups.internals

import au.id.tmm.lawsplayground.semigroups.{Instance, LawTestResult, TypeClass}

private[semigroups] object Graphing {

  def dotSnippetFor(instance: Instance[_], typeClasses: Set[TypeClass], resultsPerTypeClass: TypeClass => LawTestResult): String = {
    makeDotSnippet(
      graphName = instance.name.replaceAll("""\W""", ""),
      typeClasses.toList.sortBy(_.name),
      colouring = t => resultsPerTypeClass(t) match {
        case LawTestResult.Pass => Some("green") // TODO this is not really accessible
        case LawTestResult.Fail => Some("red") // TODO this is not really accessible
      }
    )
  }

  private def nodeNameFor(typeClass: TypeClass): String =
    typeClass.name.replaceAll("\\W", "")

  private def makeDotSnippet(graphName: String, typeClasses: List[TypeClass], colouring: TypeClass => Option[String]): String = {
    val nodesSnippet =
      typeClasses
        .map { t =>
          colouring(t) match {
            case Some(colour) => s"""${nodeNameFor(t)} [color=$colour, style=filled, label="${t.name}"]"""
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

            s"""${nodeNameFor(parent)} -> ${nodeNameFor(child)} [ label = "$lawNamesForEdge" ]"""
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
