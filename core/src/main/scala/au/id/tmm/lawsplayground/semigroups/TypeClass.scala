package au.id.tmm.lawsplayground.semigroups

final case class TypeClass(
  name: String,
  parents: Set[TypeClass],
  laws: Set[Law],
)
