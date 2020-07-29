package au.id.tmm.lawsplayground.semigroups.presentation

object StringOps {

  def interleave(left: String, right: String): String =
    (left zip right).flatMap {
      case (leftChar, rightChar) => List(leftChar, rightChar)
    }.mkString

}
