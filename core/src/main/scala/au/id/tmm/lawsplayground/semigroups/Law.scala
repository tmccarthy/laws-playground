package au.id.tmm.lawsplayground.semigroups

import cats.kernel.laws.IsEq

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


