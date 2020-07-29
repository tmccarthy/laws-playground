package au.id.tmm.lawsplayground.semigroups.presentation

import au.id.tmm.lawsplayground.semigroups.{Instance, LawsPlayground, TypeClass}
import cats.data.NonEmptyList
import cats.laws.discipline.arbitrary._
import spire.math.Rational
import spire.laws.arb._

object Presentation extends LawsPlayground {
  override def instances: List[Instance[_]] =
    List(
      Instance[Int](
        name = "Int under addition",
        binaryOp = (i1: Int, i2: Int) => i1 + i2,
      ),
      Instance[Int](
        name = "Int under maximum",
        binaryOp = (i1: Int, i2: Int) => i1 max i2,
      ),
      Instance[Rational](
        name = "Rational under addition",
        binaryOp = (r1: Rational, r2: Rational) => r1 + r2,
      ),
      Instance[String](
        name = "String under concatenation",
        binaryOp = (s1: String, s2: String) => s1 + s2,
      ),
      Instance[String](
        name = "String under interleave",
        binaryOp = (s1: String, s2: String) => StringOps.interleave(s1, s2),
      ),
      Instance[List[String]](
        binaryOp = (l1: List[String], l2: List[String]) => l1 ++ l2,
      ),
      Instance[NonEmptyList[String]](
        binaryOp = (nel1: NonEmptyList[String], nel2: NonEmptyList[String]) => nel1.concatNel(nel2),
      ),
      Instance[Set[String]](
        binaryOp = (l1: Set[String], l2: Set[String]) => l1 ++ l2,
      ),
      Instance[SquareMatrix](
        binaryOp = (m1: SquareMatrix, m2: SquareMatrix) => m1 + m2,
      ),
    )

  override def typeClasses: List[TypeClass] = {

    List(
    )
  }
}
