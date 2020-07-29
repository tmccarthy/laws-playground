package au.id.tmm.lawsplayground.semigroups

import java.util.concurrent.Executors

import au.id.tmm.lawsplayground.semigroups.internals.{Rendering, Testing}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

abstract class LawsPlayground {

  def instances: List[Instance[_]]

  def typeClasses: List[TypeClass]

  final def main(args: Array[String]): Unit = {
    implicit val ec: ExecutionContextExecutor = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(Runtime.getRuntime.availableProcessors()))

    val testResult = Testing().testAllLaws(instances, typeClasses.toSet)

    Rendering.render(testResult)
  }

}
