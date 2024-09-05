package au.id.tmm.lawsplayground.semigroups

import au.id.tmm.lawsplayground.semigroups.internals.{Rendering, Testing}

abstract class LawsPlayground {

  def instances: List[Instance[_]]

  def typeClasses: List[TypeClass]

  final def main(args: Array[String]): Unit = {
    val testResult = Testing().testAllLaws(instances, typeClasses.toSet)

    Rendering.render(testResult)
  }

}
