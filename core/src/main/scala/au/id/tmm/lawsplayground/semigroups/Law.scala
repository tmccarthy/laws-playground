package au.id.tmm.lawsplayground.semigroups

import cats.kernel.laws.IsEq

sealed abstract class Law {
  def name: String
}

// TODO a law that is composed of other laws, so we can test commutative identity?
object Law {

  abstract class With1Param(val name: String) extends Law {
    def test[A : Instance](a: A): IsEq[A]
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


