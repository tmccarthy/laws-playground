package au.id.tmm.lawsplayground.semigroups

// TODO rename or remove
object Runner extends LawsPlayground {

  def play: (List[Instance[_]], List[TypeClass]) = {
    val forInt: Instance[Int]       = Instance(binaryOp = _ + _, identity = 0)
    val forIntMax: Instance[Int]    = Instance(name = "Int with max", binaryOp = _ max _, identity = Int.MinValue)
    val forString: Instance[String] = Instance(binaryOp = _ + _, identity = "")
    val forSet: Instance[Set[Int]]  = Instance(binaryOp = _ ++ _, identity = Set.empty)

    val magma = TypeClass(
      name = "magma",
      parents = Set.empty,
      laws = Set.empty,
    )

    val semigroup = TypeClass(
      name = "semigroup",
      parents = Set(magma),
      laws = Set(Laws.associativity),
    )

    val monoid = TypeClass(
      name = "monoid",
      parents = Set(semigroup),
      laws = Set(Laws.additiveIdentity),
    )

    val band = TypeClass(
      name = "band",
      parents = Set(semigroup),
      laws = Set(Laws.idempotence),
    )

    (List(forInt, forIntMax, forString, forSet), List(magma, semigroup, monoid, band))
  }

}
