package xyz

import org.scalacheck.{Gen, Prop, Properties}

object ClaimTest extends Properties("ClaimTest") {

  val (x, y) = (1, 2)
  val (s0, s1) = ("hello", "goodbye")

  val ws = List.empty[Int]
  val xs = List(1, 2, 3, 4)
  val ys = Set(1, 2, 3)
  val zs = Map("foo" -> 1)

  def run(p: Prop): Either[Set[String], Set[String]] = {
    val r = p.apply(Gen.Parameters.default)
    val passed = r.status == Prop.True || r.status == Prop.Proof
    if (passed) Right(r.labels) else Left(r.labels)
  }

  def test(p: Prop, msg: String): Prop =
    Claim(run(p) == Left(Set(msg)))

  property("x == y")   = test(Claim(x == y),   "falsified: 1 == 2")
  property("x != x")   = test(Claim(x != x),   "falsified: 1 != 1")
  property("s0 eq s1") = test(Claim(s0 eq s1), "falsified: hello eq goodbye")
  property("s0 ne s0") = test(Claim(s0 ne s0), "falsified: hello ne hello")

  property("x < x")  = test(Claim(x < x),  "falsified: 1 < 1")
  property("y <= x") = test(Claim(y <= x), "falsified: 2 <= 1")
  property("x > x")  = test(Claim(x > x),  "falsified: 1 > 1")
  property("x >= y") = test(Claim(x >= y), "falsified: 1 >= 2")

  property("xs.size == 0") =
    test(Claim(xs.size == 0), "falsified: List(1, 2, 3, 4).size {4} == 0")

  property("ys.size == 2") =
    test(Claim(ys.size == 2), "falsified: Set(1, 2, 3).size {3} == 2")

  property("zs.size == 3") =
    test(Claim(zs.size == 3), "falsified: Map(foo -> 1).size {1} == 3")

  property("xs.length == 0") =
    test(Claim(xs.length == 0), "falsified: List(1, 2, 3, 4).length {4} == 0")

  property("s0 compare s1") =
    test(Claim((s0 compare s1) == 0), "falsified: hello.compare(goodbye) {1} == 0")

  property("s0 compareTo s1") =
    test(Claim((s0 compareTo s1) == 0), "falsified: hello.compareTo(goodbye) {1} == 0")

  property("xs.lengthCompare(1) == 0") =
    test(Claim(xs.lengthCompare(1) == 0), "falsified: List(1, 2, 3, 4).lengthCompare(1) {1} == 0")

  property("xs.isEmpty") =
    test(Claim(xs.isEmpty), "falsified: List(1, 2, 3, 4).isEmpty")

  property("ws.nonEmpty") =
    test(Claim(ws.nonEmpty), "falsified: List().nonEmpty")

  property("hello.startsWith(Hell)") =
    test(Claim(s0.startsWith("Hell")), "falsified: hello.startsWith(Hell)")

  property("hello.endsWith(Ello)") =
    test(Claim(s0.endsWith("Ello")), "falsified: hello.endsWith(Ello)")

  property("xs.contains(99)") =
    test(Claim(xs.contains(99)), "falsified: List(1, 2, 3, 4).contains(99)")

  property("xs.containsSlice(List(4,5)") =
    test(Claim(xs.containsSlice(List(4,5))), "falsified: List(1, 2, 3, 4).containsSlice(List(4, 5))")

  property("ys(99)") =
    test(Claim(ys(99)), "falsified: Set(1, 2, 3).apply(99)")

  property("xs.isDefinedAt(6)") =
    test(Claim(xs.isDefinedAt(6)), "falsified: List(1, 2, 3, 4).isDefinedAt(6)")

  property("xs.sameElements(List(1,2,3,5))") =
    test(Claim(xs.sameElements(List(1,2,3,5))), "falsified: List(1, 2, 3, 4).sameElements(List(1, 2, 3, 5))")

  property("ys.subsetOf(Set(3,4,5))") =
    test(Claim(ys.subsetOf(Set(3,4,5))), "falsified: Set(1, 2, 3).subsetOf(Set(3, 4, 5))")

  property("xs.exists(_ < 0)") =
    test(Claim(xs.exists(_ < 0)), "falsified: List(1, 2, 3, 4).exists(...)")

  property("xs.forall(_ > 1)") =
    test(Claim(xs.forall(_ > 1)), "falsified: List(1, 2, 3, 4).forall(...)")

  property("x < x && x < y") =
    test(Claim(x < x && x < y), "falsified: (1 < 1 {false}) && (1 < 2 {true})")

  property("x < x || y < y") =
    test(Claim(x < x || y < y), "falsified: (1 < 1 {false}) || (2 < 2 {false})")

  property("(x < y) ^ (y > x)") =
    test(Claim((x < y) ^ (y > x)), "falsified: (1 < 2 {true}) ^ (2 > 1 {true})")

  property("!(x < y)") =
    test(Claim(!(x < y)), "falsified: !(1 < 2 {true})")

  property("xs.filter(_ > 2).isEmpty") =
    test(Claim(xs.filter(_ > 2).isEmpty), "falsified: List(3, 4).isEmpty")

  property("ys.map(_ + 1).subsetOf(ys)") =
    test(Claim(ys.map(_ + 1).subsetOf(ys)), "falsified: Set(2, 3, 4).subsetOf(Set(1, 2, 3))")

  property("xs.min == 2") =
    test(Claim(xs.min == 2), "falsified: List(1, 2, 3, 4).min {1} == 2")

  property("xs.max == 3") =
    test(Claim(xs.max == 3), "falsified: List(1, 2, 3, 4).max {4} == 3")

  val (n1, n2, n3) = (0.29622045F, -8.811786E-7F, 1.0369974E-8F)

  property("(n1 + (n2 + n3)) == ((n1 + n2) + n3)") =
    test(Claim((n1 + (n2 + n3)) == ((n1 + n2) + n3)), "falsified: 0.2962196 == 0.29621956")
}