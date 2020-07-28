package au.id.tmm.lawsplayground.semigroups.internals

import au.id.tmm.lawsplayground.semigroups.{Instance, Law, LawTestResult, TypeClass}
import au.id.tmm.utilities.syntax.tuples.->
import cats.kernel.Eq
import cats.kernel.laws.discipline.catsLawsIsEqToProp
import org.scalacheck.{Arbitrary, Gen, Prop}

// TODO logging
object Testing {

  final case class Result(
    typeClasses: Set[TypeClass],
    resultsPerTypeClassPerInstance: List[Instance[_] -> Map[TypeClass, LawTestResult]],
  )

  private[semigroups] def testAllLaws(instances: List[Instance[_]], typeClasses: Set[TypeClass]): Result = {

    val resultsPerTypeClassPerInstance = instances.map { instance =>
      val resultPerTypeClass: Map[TypeClass, LawTestResult] = typeClasses.map { typeClass =>
        val resultForTypeclass: LawTestResult =
          typeClass.laws
            .map { law =>
              doTestsUnsafe(law, instance)
            }
            .foldLeft[LawTestResult](LawTestResult.Pass)(_ && _)

        typeClass -> resultForTypeclass
      }.toMap

      instance -> resultPerTypeClass
    }

    Result(
      typeClasses,
      resultsPerTypeClassPerInstance,
    )
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
