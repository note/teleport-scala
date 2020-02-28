import $ivy.`com.lihaoyi::os-lib:0.6.3`
import $ivy.`com.lihaoyi::utest:0.7.2`

import utest._

val testDir = os.pwd / "teleport_guinea_pig"

if(os.exists(testDir)) {
  println(s"$testDir already exists")
  sys.exit(2)
}

os.makeDir(testDir)
List("tdir_a", "tdir_b", "tdir_c").map(testDir / _).foreach(os.makeDir)

val tests = Tests{
  Symbol("`list` should return status code 0 if $HOME/.teleport-data does not exists") - {
    verify(List("list"), 0)
  }
  Symbol("`add` should return status code 0 for existing dir") - {
    verify(List("add", "tpoint0", "tdir_a"), 0)
    verify(List("add", "tpoint1", "tdir_b"), 0)
  }
  Symbol("`add` should return status code 1 for non existing dir") - {
    verify(List("add", "tpointX", "nonexistent"), 1)
  }
  Symbol("`list` should confirm that all previous points were created") - {
    val listResult = verify(List("list"), 0)

    assert(listResult.find(l => l.contains("tpoint0") && l.contains("tdir_a")).isDefined,
           listResult.find(l => l.contains("tpoint1") && l.contains("tdir_b")).isDefined,
           listResult.find(_.contains("tpointX")).isEmpty,
           listResult.find(_.contains("tpointX")).isEmpty)
  }
  Symbol("`--no-headers list` should not contain headers")- {
    val listResult = verify(List("list"), 0)
    assert(listResult.size == 3, s"size == ${listResult.size}, expected: 3")

    val noheadersListResult = verify(List("--no-headers", "list"), 0)

    assert(noheadersListResult.size == 2, s"size == ${noheadersListResult.size}, expected: 2")
  }
  Symbol("`--no-colors list` should not contain colors") - {
    val colorlessListResult = verify(List("--no-colors", "list"), 0)
    val noheadersListResult = verify(List("--no-headers", "list"), 0)
    val expectedLine = "tpoint0\t/root/teleport_guinea_pig/tdir_a"
    assert(colorlessListResult.contains(expectedLine))
    assert(!noheadersListResult.contains(expectedLine))
  }
}

val result = TestRunner.runAndPrint(tests, "teleport-scala")
val rendered = TestRunner.renderResults(List("teleport-scala" -> result))

println(rendered._1.render)

if(rendered._3 > 0) {
  sys.exit(1)
} else {
  sys.exit(0)
}

def verify(arguments: List[String], expectedExitCode: Int): Vector[String] = {
  val cmd = "../teleport-scala"
  val res = os.proc(cmd :: arguments).call(cwd = os.pwd / "teleport_guinea_pig", check = false)
  scala.Predef.assert(res.exitCode == expectedExitCode, s"Unexpected status code: ${res.exitCode}, expected: ${expectedExitCode} for $cmd")
  res.out.lines
}

def fail(msg: String) = {
  println(msg)
  sys.exit(1)
}
