package au.id.tmm.lawsplayground.semigroups

import cats.data.NonEmptyList
import cats.kernel.Eq
import cats.kernel.laws.discipline.catsLawsIsEqToProp
import cats.kernel.laws.{IsEq, IsEqArrow}
import org.scalacheck.{Arbitrary, Gen, Prop}

sealed abstract class Law {
  def name: String
}

object Law {

  type Params3[A] = Tuple3[A, A, A]

  abstract class With1Param(val name: String) extends Law {
    def test[A : Instance](a1: A): IsEq[A]
  }

  abstract class With2Params(val name: String) extends Law {
    def test[A : Instance](a1: A, a2: A): IsEq[A]
  }

  abstract class With3Params(val name: String) extends Law {
    def test[A : Instance](
      a1: A,
      a2: A,
      a3: A,
    ): IsEq[A]
  }

  abstract class With4Params(val name: String) extends Law {
    def test[A : Instance](
      a1: A,
      a2: A,
      a3: A,
      a4: A,
    ): IsEq[A]
  }

  abstract class With5Params(val name: String) extends Law {
    def test[A : Instance](
      a1: A,
      a2: A,
      a3: A,
      a4: A,
      a5: A,
    ): IsEq[A]
  }

}

object Laws {
  import syntax._

  val consistency: Law =
    new Law.With2Params("consistency") {
      def test[A : Instance](a1: A, a2: A): IsEq[A] =
        (a1 |+| a2) <-> (a1 |+| a2)
    }

  val associativity: Law =
    new Law.With3Params("associativity") {
      def test[A : Instance](
        a1: A,
        a2: A,
        a3: A,
      ): IsEq[A] =
        ((a1 |+| a2) |+| a3) <-> (a1 |+| (a2 |+| a3))
    }

  val additiveIdentity: Law =
    new Law.With1Param("additive identity") {
      def test[A : Instance](a: A): IsEq[A] =
        a <-> (a |+| zero)
    }

}

object Runner {

  def main(args: Array[String]): Unit = {
    val forInt: Instance[Int]       = Instance(binaryOp = _ + _)
    val forString: Instance[String] = Instance(binaryOp = _ + _)

    val magma = TypeClass(
      name = "magma",
      parents = Set.empty,
      laws = NonEmptyList.of(Laws.consistency),
    )

    val semigroup = TypeClass(
      name = "semigroup",
      parents = Set(magma),
      laws = NonEmptyList.of(Laws.associativity),
    )

    val monoid = TypeClass(
      name = "monoid",
      parents = Set(semigroup),
      laws = NonEmptyList.of(Laws.additiveIdentity),
    )

    run(List(forInt, forString), List(magma, semigroup, monoid))
  }

  def run(
    instances: List[Instance[_]],
    typeClasses: List[TypeClass],
  ): Unit =
    instances.foreach { instance =>
      typeClasses.foreach { typeClass =>
        typeClass.laws.toList.foreach { law =>
          doTests[Nothing](law, instance.asInstanceOf[Instance[Nothing]])
        }
      }
    }

  def doTests[A](law: Law, instance: Instance[A]): Unit = {
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

    if (result.success)
      println(s"✅ ${instance.name} complies with ${law.name}")
    else
      println(s"❌ ${instance.name} does not comply with ${law.name}")
  }

}
