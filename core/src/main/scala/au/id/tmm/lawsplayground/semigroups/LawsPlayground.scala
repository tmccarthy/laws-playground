package au.id.tmm.lawsplayground.semigroups

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Path}

import cats.kernel.Eq
import cats.kernel.laws.discipline.catsLawsIsEqToProp
import org.scalacheck.{Arbitrary, Gen, Prop}

abstract class LawsPlayground {

  def play: (List[Instance[_]], List[TypeClass])

  final def main(args: Array[String]): Unit = {

    val (instances, typeClasses) = play match {
      case (instances, typeClasses) => (instances, typeClasses.toSet)
    }

    val dotSnippetPerInstance: List[(Instance[_], String)] =
      instances.map { instance =>
        val resultPerTypeClass: Map[TypeClass, LawTestResult] = typeClasses.map { typeClass =>
          val resultForTypeclass: LawTestResult =
            typeClass.laws
              .map { law =>
                doTestsUnsafe(law, instance)
              }
              .reduce

          typeClass -> resultForTypeclass
        }.toMap

        val dotSnippet = Graphing.dotSnippetFor(instance, typeClasses, resultPerTypeClass)

        instance -> dotSnippet
      }

    val dotsArray = dotSnippetPerInstance.map(_._2).map(_.replace("\n", "\\n").replace("\"", "\\\"")).mkString("[\"", "\", \n\"", "\"]")
    val title = "Laws graph"

    val html =
      s"""<!DOCTYPE html>
         |<html lang="en">
         |<head>
         |    <meta charset="utf-8">
         |    <meta content="text/html;charset=utf-8" http-equiv="Content-Type">
         |    <meta content="utf-8" http-equiv="encoding">
         |    <title>$title</title>
         |    <script src="https://d3js.org/d3.v5.min.js"></script>
         |    <script src="https://unpkg.com/@hpcc-js/wasm@0.3.11/dist/index.min.js"></script>
         |    <script src="https://unpkg.com/d3-graphviz@3.0.5/build/d3-graphviz.js"></script>
         |</head>
         |<body>
         |<div id="graph" style="text-align: center;"></div>
         |<script type="text/javascript">
         |    var dotIndex = 0;
         |    var graphviz = d3.select("#graph").graphviz()
         |        .transition(function () {
         |            return d3.transition("main")
         |                .ease(d3.easeLinear)
         |                .delay(500)
         |                .duration(1500);
         |        })
         |        .logEvents(true)
         |        .on("initEnd", render);
         |
         |
         |    function render() {
         |        var dot = dots[dotIndex];
         |        graphviz
         |            .renderDot(dot)
         |            .on("end", function () {
         |                dotIndex = (dotIndex + 1) % dots.length;
         |                render();
         |            });
         |    }
         |
         |    var dots = $dotsArray;
         |</script>
         |</body>
         |</html>
         |""".stripMargin

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

}
