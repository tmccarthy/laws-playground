package au.id.tmm.lawsplayground.semigroups.presentation

import org.scalatest.flatspec.AnyFlatSpec

class StringOpsSpec extends AnyFlatSpec {

  "interleave" should "interleave two strings of the same length" in {
    assert(StringOps.interleave("abc", "123") === "a1b2c3")
  }

  it should "interleave two strings of different lengths" in {
    assert(StringOps.interleave("abc", "12345") === "a1b2c3")
  }

  it should "interleave when one string is empty" in {
    assert(StringOps.interleave("abc", "") === "")
  }

}
