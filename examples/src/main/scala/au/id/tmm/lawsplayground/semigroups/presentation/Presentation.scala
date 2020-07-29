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
        identity = 0,
        inverse = (i: Int) => -i,
        multiplicativeOp = (i1: Int, i2: Int) => i1 * i2,
        multiplicativeIdentity = 1,
        multiplicativeInverse = (i: Int) => i match {
          case 0 => None
          case i => Some(1 / i)
        }
      ),
      Instance[Double](
        name = "Double under addition",
        binaryOp = (d1: Double, d2: Double) => d1 + d2,
        identity = 0d,
        inverse = (d: Double) => -d,
        multiplicativeOp = (d1: Double, d2: Double) => d1 * d2,
        multiplicativeIdentity = 1d,
        multiplicativeInverse = (d: Double) => d match {
          case 0 => None
          case d => Some(1 / d)
        }
      ),
      Instance[Rational](
        name = "Rational under addition",
        binaryOp = (r1: Rational, r2: Rational) => r1 + r2,
        identity = 0d,
        inverse = (r: Rational) => r * -1,
        multiplicativeOp = (r1: Rational, r2: Rational) => r1 * r2,
        multiplicativeIdentity = 1d,
        multiplicativeInverse = (r: Rational) => r match {
          case Rational.zero => None
          case r => Some(r.inverse)
        }
      ),
      Instance[Int](
        name = "Int under maximum",
        binaryOp = (i1: Int, i2: Int) => i1 max i2,
        identity = Int.MinValue,
      ),
      Instance[String](
        binaryOp = (s1: String, s2: String) => s1 + s2,
        identity = "",
      ),
      Instance[List[String]](
        binaryOp = (l1: List[String], l2: List[String]) => l1 ++ l2,
        identity = List.empty,
      ),
      Instance[NonEmptyList[String]](
        binaryOp = (nel1: NonEmptyList[String], nel2: NonEmptyList[String]) => nel1.concatNel(nel2),
      ),
      Instance[Set[String]](
        binaryOp = (l1: Set[String], l2: Set[String]) => l1 ++ l2,
        identity = Set.empty[String],
      ),
      Instance[SquareMatrix](
        binaryOp = (m1: SquareMatrix, m2: SquareMatrix) => m1 + m2,
        identity = SquareMatrix.zero,
        inverse = (m: SquareMatrix) => m * -1,
        multiplicativeOp = (m1: SquareMatrix, m2: SquareMatrix) => m1 * m2,
        multiplicativeIdentity = SquareMatrix.I,
      ),
    )

  override def typeClasses: List[TypeClass] = {
    val semigroup = TypeClass(
      name = "Semigroup",
      parents = Set.empty,
      laws = Set(
        Laws.associativity,
      ),
    )

    val monoid = TypeClass(
      name = "Monoid",
      parents = Set(semigroup),
      laws = Set(
        Laws.identity,
      ),
    )

    val band = TypeClass(
      name = "Band",
      parents = Set(semigroup),
      laws = Set(
        Laws.idempotence,
      ),
    )

    val semilattice = TypeClass(
      name = "Semilattice",
      parents = Set(band),
      laws = Set(
        Laws.commutativity,
      ),
    )

    val commutativeMonoid = TypeClass(
      name = "Commutative Monoid",
      parents = Set(monoid),
      laws = Set(
        Laws.commutativity,
      ),
    )

    val boundedSemilattice = TypeClass(
      name = "Bounded Semilattice",
      parents = Set(semilattice, commutativeMonoid),
      laws = Set.empty,
    )

    val group = TypeClass(
      name = "Group",
      parents = Set(monoid),
      laws = Set(
        Laws.inverse,
      ),
    )

    val commutativeGroup = TypeClass(
      name = "Commutative Group",
      parents = Set(group, commutativeMonoid),
      laws = Set.empty,
    )

    val ring = TypeClass(
      name = "Ring",
      parents = Set(commutativeGroup),
      laws = Set(
        Laws.associativeMultiplication,
        Laws.multiplicativeIdentity,
        Laws.distributive,
      ),
    )

    val field = TypeClass(
      name = "Field",
      parents = Set(ring),
      laws = Set(
        Laws.multiplicativeInverse,
      ),
    )

    List(
      semigroup,
      monoid,
      band,
      semilattice,
      commutativeMonoid,
      boundedSemilattice,
      group,
      commutativeGroup,
      ring,
      field,
    )
  }
}
