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
        },
      ),
      Instance[Double](
        name = "Double under addition",
        binaryOp = (i1: Double, i2: Double) => i1 + i2,
        identity = 0d,
        inverse = (i: Double) => -i,
        multiplicativeOp = (i1: Double, i2: Double) => i1 * i2,
        multiplicativeIdentity = 1,
        multiplicativeInverse = (i: Double) => i match {
          case 0d => None
          case i => Some(1 / i)
        },
      ),
      Instance[Int](
        name = "Int under maximum",
        binaryOp = (i1: Int, i2: Int) => i1 max i2,
        identity = Int.MinValue,
      ),
      Instance[Rational](
        name = "Rational under addition",
        binaryOp = (r1: Rational, r2: Rational) => r1 + r2,
        identity = 0,
        inverse = (r: Rational) => r * (-1),
        multiplicativeOp = (r1: Rational, r2: Rational) => r1 * r2,
        multiplicativeIdentity = 1,
        multiplicativeInverse = (r: Rational) => r match {
          case Rational.zero => None
          case r => Some(1 / r)
        },
      ),
      Instance[String](
        name = "String under concatenation",
        binaryOp = (s1: String, s2: String) => s1 + s2,
        identity = "",
      ),
      Instance[String](
        name = "String under interleave",
        binaryOp = (s1: String, s2: String) => StringOps.interleave(s1, s2),
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
    val magma = TypeClass(
      name = "Magma",
      parents = Set.empty,
      laws = Set(
        Laws.binaryOperation,
      )
    )

    val semigroup = TypeClass(
      name = "Semigroup",
      parents = Set(magma),
      laws = Set(
        Laws.associative,
      )
    )

    val monoid = TypeClass(
      name = "Monoid",
      parents = Set(semigroup),
      laws = Set(
        Laws.identity,
      )
    )

    val band = TypeClass(
      name = "Band 🥁",
      parents = Set(semigroup),
      laws = Set(
        Laws.idempotence,
      )
    )

    val commutativeMonoid = TypeClass(
      name = "Commutative Monoid",
      parents = Set(monoid),
      laws = Set(
        Laws.commutative,
      )
    )

    val semilattice = TypeClass(
      name = "Semilattice",
      parents = Set(band),
      laws = Set(
        Laws.commutative,
      )
    )

    val group = TypeClass(
      name = "Group",
      parents = Set(monoid),
      laws = Set(
        Laws.inverse,
      ),
    )

    val commutativeGroup = TypeClass(
      name = "Abelian Group 😱",
      parents = Set(commutativeMonoid, group),
      laws = Set.empty,
    )

    val rng = TypeClass(
      name = "Rng",
      parents = Set(group),
      laws = Set(
        Laws.associativeMultiplicativeBinaryOp,
        Laws.distributive,
      )
    )

    val ring = TypeClass(
      name = "Ring",
      parents = Set(rng),
      laws = Set(
        Laws.multiplicativeIdentity,
      )
    )

    val field = TypeClass(
      name = "Field 🥅",
      parents = Set(ring),
      laws = Set(
        Laws.multiplicativeInverse,
      )
    )

    List(
      magma,
      semigroup,
      monoid,
      band,
      commutativeMonoid,
      semilattice,
      group,
      commutativeGroup,
      rng,
      ring,
      field,
    )
  }
}
