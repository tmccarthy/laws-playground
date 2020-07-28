package au.id.tmm.lawsplayground.semigroups.presentation

import au.id.tmm.lawsplayground.semigroups.syntax._
import au.id.tmm.lawsplayground.semigroups.{Instance, Law}
import cats.kernel.laws.IsEq

object Laws {

  val associativity: Law = new Law.With3Params(name = "associativity") {
    override def test[A: Instance](a1: A, a2: A, a3: A): IsEq[A] =
      ((a1 + a2) + a3) alwaysEquals (a1 + (a2 + a3))
  }

  val identity: Law = new Law.With1Param(name = "identity") {
    override def test[A: Instance](a: A): IsEq[A] =
      (a + zero) alwaysEquals a
  }

  val idempotence: Law = new Law.With1Param(name = "idempotence") {
    override def test[A: Instance](a: A): IsEq[A] =
      (a + a) alwaysEquals a
  }

  val commutativity: Law = new Law.With2Params(name = "commutativity") {
    override def test[A: Instance](a1: A, a2: A): IsEq[A] =
      (a1 + a2) alwaysEquals (a2 + a1)
  }

  val inverse: Law = new Law.With1Param(name = "inverse") {
    override def test[A: Instance](a: A): IsEq[A] =
      (a + (-a)) alwaysEquals a
  }

}
