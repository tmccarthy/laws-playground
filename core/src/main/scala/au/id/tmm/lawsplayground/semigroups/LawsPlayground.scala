package au.id.tmm.lawsplayground.semigroups

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path}

import au.id.tmm.utilities.syntax.tuples.->
import cats.kernel.Eq
import cats.kernel.laws.discipline.catsLawsIsEqToProp
import org.apache.commons.io.IOUtils
import org.apache.commons.text.StringSubstitutor
import org.scalacheck.{Arbitrary, Gen, Prop}
import io.circe.syntax.EncoderOps

import scala.jdk.CollectionConverters._

abstract class LawsPlayground {

  def play: (List[Instance[_]], List[TypeClass])

  final def main(args: Array[String]): Unit = {

    val (instances, typeClasses) = play match {
      case (instances, typeClasses) => (instances, typeClasses.toSet)
    }

    val dotSnippetPerInstanceName: List[String -> String] =
      instances.map { instance =>
        val resultPerTypeClass: Map[TypeClass, LawTestResult] = typeClasses.map { typeClass =>
          val resultForTypeclass: LawTestResult =
            typeClass.laws
              .map { law =>
                doTestsUnsafe(law, instance)
              }
              .foldLeft[LawTestResult](LawTestResult.Pass)(_ && _)

          typeClass -> resultForTypeclass
        }.toMap

        val dotSnippet = Graphing.dotSnippetFor(instance, typeClasses, resultPerTypeClass)

        instance.name -> dotSnippet
      }

    val html = generateHtml(
      title = "Laws graph",
      dotSnippetPerInstanceName,
    )

    val htmlFile: Path = Files.createTempFile("laws_graph", ".html")

    Files.write(htmlFile, html.getBytes(StandardCharsets.UTF_8))

    java.awt.Desktop.getDesktop.browse(htmlFile.toUri)
  }

  private def doTestsUnsafe(law: Law, instance: Instance[_]): LawTestResult =
    doTests[Nothing](law, instance.asInstanceOf[Instance[Nothing]])

  private def doTests[A](law: Law, instance: Instance[A]): LawTestResult = {
    import org.scalacheck.Prop._

    implicit val implicitInstance: Instance[A] = instance
    implicit val implicitArb: Arbitrary[A]     = instance.arbA
    implicit val implicitEq: Eq[A]             = instance.eqA

    val prop: Prop = law match {
      case lawWith1Param: Law.With1Param   => forAll(lawWith1Param.test[A] _)
      case lawWith2Params: Law.With2Params => forAll(lawWith2Params.test[A] _)
      case lawWith3Params: Law.With3Params => forAll(lawWith3Params.test[A] _)
      case lawWith4Params: Law.With4Params => forAll(lawWith4Params.test[A] _)
      case lawWith5Params: Law.With5Params => forAll(lawWith5Params.test[A] _)
    }

    val result = prop.apply(Gen.Parameters.default)

    if (result.success) LawTestResult.Pass else LawTestResult.Fail
  }

  private def generateHtml(
    title: String,
    dotSnippetPerInstanceName: List[String -> String],
  ): String = {
    val template = IOUtils.toString(getClass.getResourceAsStream("typeclass-graph.html"), StandardCharsets.UTF_8)

    val templateValues: Map[String, String] = Map(
      "title" -> title,
      "instanceNames" -> dotSnippetPerInstanceName.map(_._1).asJson.noSpaces,
      "dotsPerInstance" -> dotSnippetPerInstanceName.toMap.asJson.noSpaces,
    )

    new StringSubstitutor(templateValues.asJava).replace(template)
  }

}
