package au.id.tmm.lawsplayground.semigroups

import au.id.tmm.lawsplayground.semigroups.LawTestResult.{Fail, Pass}
import cats.kernel.Semigroup

sealed trait LawTestResult {
  def || (that: LawTestResult): LawTestResult = if (this == Pass || that == Pass) Pass else Fail
  def && (that: LawTestResult): LawTestResult = if (this == Pass && that == Pass) Pass else Fail
}

object LawTestResult {
  case object Pass extends LawTestResult
  case object Fail extends LawTestResult

  implicit val semigroup: Semigroup[LawTestResult] = _ && _
}
