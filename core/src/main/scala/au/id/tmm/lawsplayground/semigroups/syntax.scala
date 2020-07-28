package au.id.tmm.lawsplayground.semigroups

import cats.kernel.laws.IsEq

object syntax extends Instance.Syntax {
  implicit class IsEqInfix[A](a: A) {
    def alwaysEquals(that: A): IsEq[A] = IsEq(a, that)
  }
}
