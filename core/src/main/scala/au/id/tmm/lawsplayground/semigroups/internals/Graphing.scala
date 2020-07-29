package au.id.tmm.lawsplayground.semigroups.internals

import au.id.tmm.lawsplayground.semigroups.{Instance, LawTestResult, TypeClass}

private[semigroups] object Graphing {

  def dotSnippetFor(
    instance: Instance[_],
    typeClasses: Set[TypeClass],
    resultsPerTypeClass: TypeClass => LawTestResult,
  ): String =
    makeDotSnippet(
      graphName = instance.name.replaceAll("""\W""", ""),
      typeClasses.toList.sortBy(_.name),
      passOrFail = t =>
        resultsPerTypeClass(t) match {
          case LawTestResult.Pass => true
          case LawTestResult.Fail => false
        },
    )

  private def nodeNameFor(typeClass: TypeClass): String =
    typeClass.name.replaceAll("\\W", "")

  private def makeDotSnippet(
    graphName: String,
    typeClasses: List[TypeClass],
    passOrFail: TypeClass => Boolean,
  ): String = {
    val nodesSnippet =
      typeClasses
        .map { t =>
          passOrFail(t) match {
            case true  => s"""${nodeNameFor(t)} [color="#00800040", style=filled, label="✅ ${t.name}"]"""
            case false => s"""${nodeNameFor(t)} [color="#ff000040", style=filled, label="❌ ${t.name}"]"""
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
