package au.id.tmm.lawsplayground.semigroups

import au.id.tmm.lawsplayground.semigroups
import cats.kernel.laws.{IsEq, IsEqArrow}

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

  val idempotence: Law =
    new semigroups.Law.With1Param("idempotence") {
      override def test[A: Instance](a: A): IsEq[A] = (a |+| a) <-> a
    }

}
