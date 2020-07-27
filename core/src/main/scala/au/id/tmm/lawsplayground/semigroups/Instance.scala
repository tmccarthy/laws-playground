package au.id.tmm.lawsplayground.semigroups

import cats.kernel.Eq
import org.scalacheck.Arbitrary

import scala.reflect.ClassTag

final class Instance[A] private (
  val name: String,
  val arbA: Arbitrary[A],
  val eqA: Eq[A],
  val binaryOp: Option[(A, A) => A],
  val identity: Option[A],
  val inverse: Option[A => A],
  val multiplicativeOp: Option[(A, A) => A],
  val multiplicativeIdentity: Option[A],
  val multiplicativeInverse: Option[A => Option[A]],
)

object Instance {

  def apply[A : ClassTag : Arbitrary : Eq](): Instance[A] =
    new Instance[A](
      name = implicitly[ClassTag[A]].runtimeClass.getSimpleName,
      implicitly[Arbitrary[A]],
      Eq[A],
      binaryOp = None,
      identity = None,
      inverse = None,
      multiplicativeOp = None,
      multiplicativeIdentity = None,
      multiplicativeInverse = None,
    )

  def apply[A : ClassTag : Arbitrary : Eq](
    binaryOp: (A, A) => A,
  ): Instance[A] =
    new Instance[A](
      name = implicitly[ClassTag[A]].runtimeClass.getSimpleName,
      implicitly[Arbitrary[A]],
      Eq[A],
      Some(binaryOp),
      identity = None,
      inverse = None,
      multiplicativeOp = None,
      multiplicativeIdentity = None,
      multiplicativeInverse = None,
    )

  def apply[A : ClassTag : Arbitrary : Eq](
    binaryOp: (A, A) => A,
    identity: A,
  ): Instance[A] =
    new Instance[A](
      name = implicitly[ClassTag[A]].runtimeClass.getSimpleName,
      implicitly[Arbitrary[A]],
      Eq[A],
      Some(binaryOp),
      Some(identity),
      inverse = None,
      multiplicativeOp = None,
      multiplicativeIdentity = None,
      multiplicativeInverse = None,
    )

  def apply[A : ClassTag : Arbitrary : Eq](
    binaryOp: (A, A) => A,
    identity: A,
    inverse: A => A,
  ): Instance[A] =
    new Instance[A](
      name = implicitly[ClassTag[A]].runtimeClass.getSimpleName,
      implicitly[Arbitrary[A]],
      Eq[A],
      Some(binaryOp),
      Some(identity),
      Some(inverse),
      multiplicativeOp = None,
      multiplicativeIdentity = None,
      multiplicativeInverse = None,
    )

  def apply[A : ClassTag : Arbitrary : Eq](
    binaryOp: (A, A) => A,
    identity: A,
    inverse: A => A,
    multiplicativeOp: (A, A) => A,
  ): Instance[A] =
    new Instance[A](
      name = implicitly[ClassTag[A]].runtimeClass.getSimpleName,
      implicitly[Arbitrary[A]],
      Eq[A],
      Some(binaryOp),
      Some(identity),
      Some(inverse),
      Some(multiplicativeOp),
      multiplicativeIdentity = None,
      multiplicativeInverse = None,
    )

  def apply[A : ClassTag : Arbitrary : Eq](
    binaryOp: (A, A) => A,
    identity: A,
    inverse: A => A,
    multiplicativeOp: (A, A) => A,
    multiplicativeIdentity: A,
  ): Instance[A] =
    new Instance[A](
      name = implicitly[ClassTag[A]].runtimeClass.getSimpleName,
      implicitly[Arbitrary[A]],
      Eq[A],
      Some(binaryOp),
      Some(identity),
      Some(inverse),
      Some(multiplicativeOp),
      Some(multiplicativeIdentity),
      multiplicativeInverse = None,
    )

  def apply[A : ClassTag : Arbitrary : Eq](
    binaryOp: (A, A) => A,
    identity: A,
    inverse: A => A,
    multiplicativeOp: (A, A) => A,
    multiplicativeIdentity: A,
    multiplicativeInverse: A => Option[A],
  ): Instance[A] =
    new Instance[A](
      name = implicitly[ClassTag[A]].runtimeClass.getSimpleName,
      implicitly[Arbitrary[A]],
      Eq[A],
      Some(binaryOp),
      Some(identity),
      Some(inverse),
      Some(multiplicativeOp),
      Some(multiplicativeIdentity),
      Some(multiplicativeInverse),
    )

  // TODO replace the .get with something more sophisticated
  final class Ops[A] private[Instance] (left: A)(implicit instance: Instance[A]) {
    def |+|(right: A): A = instance.binaryOp.get.apply(left, right)

    def |*|(right: A): A = instance.multiplicativeOp.get.apply(left, right)

    def inverse: A  = instance.inverse.get.apply(left)
    def unary_- : A = inverse

    def multiplicativeInverse: Option[A] = instance.multiplicativeInverse.get.apply(left)
    def `^-1`: Option[A]                 = multiplicativeInverse
    def `⁻¹` : Option[A]                 = multiplicativeInverse
  }

  trait Syntax {
    implicit def toInstanceOps[A](a: A)(implicit instance: Instance[A]): Instance.Ops[A] =
      new Instance.Ops[A](a)

    def zero[A](implicit instance: Instance[A]): A = instance.identity.get
    def `|0|`[A](implicit instance: Instance[A]): A = zero

    def one[A](implicit instance: Instance[A]): A = instance.multiplicativeIdentity.get
    def `|1|`[A](implicit instance: Instance[A]): A = one
  }

}
