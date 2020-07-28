package au.id.tmm.lawsplayground.semigroups.internals

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path}

import au.id.tmm.utilities.syntax.tuples.->
import io.circe.syntax.EncoderOps
import org.apache.commons.io.IOUtils
import org.apache.commons.text.StringSubstitutor

import scala.jdk.CollectionConverters._

object Rendering {

  private[semigroups] def render(testingResult: Testing.Result): Unit = {
    val dotSnippetPerInstanceName: List[String -> String] =
      testingResult.resultsPerTypeClassPerInstance
        .map {
          case (instance, resultsPerTypeClass) =>
            instance.name -> Graphing.dotSnippetFor(instance, resultsPerTypeClass.keySet, resultsPerTypeClass)
        }

    val html = generateHtml(
      title = "Laws graph",
      dotSnippetPerInstanceName,
    )

    val htmlFile: Path = Files.createTempFile("laws_graph", ".html")

    Files.write(htmlFile, html.getBytes(StandardCharsets.UTF_8))

    java.awt.Desktop.getDesktop.browse(htmlFile.toUri)

  }

  private def generateHtml(
    title: String,
    dotSnippetPerInstanceName: List[String -> String],
  ): String = {
    val template = IOUtils.toString(getClass.getResourceAsStream("typeclass-graph.html"), StandardCharsets.UTF_8)

    val templateValues: Map[String, String] = Map(
      "title"           -> title,
      "instanceNames"   -> dotSnippetPerInstanceName.map(_._1).asJson.noSpaces,
      "dotsPerInstance" -> dotSnippetPerInstanceName.toMap.asJson.noSpaces,
    )

    new StringSubstitutor(templateValues.asJava).replace(template)
  }

}
