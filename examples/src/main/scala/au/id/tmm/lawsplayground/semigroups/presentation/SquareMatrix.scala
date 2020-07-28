package au.id.tmm.lawsplayground.semigroups.presentation

import cats.Show
import cats.kernel.Eq
import org.scalacheck.{Arbitrary, Gen}

final case class SquareMatrix(
  a11: Int, a12: Int, a13: Int,
  a21: Int, a22: Int, a23: Int,
  a31: Int, a32: Int, a33: Int,
) {

  def + (that: SquareMatrix): SquareMatrix =
    SquareMatrix(
      this.a11 + that.a11, this.a12 + that.a12, this.a13 + that.a13,
      this.a21 + that.a21, this.a22 + that.a22, this.a23 + that.a23,
      this.a31 + that.a31, this.a32 + that.a32, this.a33 + that.a33,
    )

  def * (that: SquareMatrix): SquareMatrix =
    SquareMatrix(
      // format: off
      this.a11*that.a11+this.a12*that.a21+this.a13*that.a31, this.a11*that.a12+this.a12*that.a22+this.a13*that.a32, this.a11*that.a13+this.a12*that.a23+this.a13*that.a33,
      this.a21*that.a11+this.a22*that.a21+this.a23*that.a31, this.a21*that.a12+this.a22*that.a22+this.a23*that.a32, this.a21*that.a13+this.a22*that.a23+this.a23*that.a33,
      this.a31*that.a11+this.a32*that.a21+this.a33*that.a31, this.a31*that.a12+this.a32*that.a22+this.a33*that.a32, this.a31*that.a13+this.a32*that.a23+this.a33*that.a33,
      // format: on
    )

  override def toString: String =
    """⎡ %3d, %3d, %3d ⎤
      |⎢ %3d, %3d, %3d ⎥
      |⎣ %3d, %3d, %3d ⎦""".stripMargin

}

object SquareMatrix {
  // format: off
  val zero: SquareMatrix = SquareMatrix(
    0, 0, 0,
    0, 0, 0,
    0, 0, 0,
  )

  val I: SquareMatrix = SquareMatrix(
    1, 0, 0,
    0, 1, 0,
    0, 0, 1,
  )
  // format: on

  implicit val eq: Eq[SquareMatrix] = Eq.fromUniversalEquals

  implicit val arbitrary: Arbitrary[SquareMatrix] = Arbitrary {
    Gen.resultOf(SquareMatrix.apply(_, _, _, _, _, _, _, _, _))
  }

  implicit val show: Show[SquareMatrix] = Show.fromToString

}