package au.id.tmm.lawsplayground.semigroups

import cats.data.NonEmptyList

final case class TypeClass(
  name: String,
  parents: Set[TypeClass],
  laws: NonEmptyList[Law],
)
