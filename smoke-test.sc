import $ivy.`com.lihaoyi::os-lib:0.6.3`
import $ivy.`com.lihaoyi::os-lib:0.2.9`

val testDir = os.pwd / "teleport_guinea_pig"

if(os.exists(testDir)) {
  println(s"$testDir already exists")
  sys.exit(2)
}

try {
  os.makeDir(testDir)
  List("tdir_a", "tdir_b", "tdir_c").map(testDir / _).foreach(os.makeDir)

  verify(List("list"), 0)

  verify(List("add", "tpoint0", "tdir_a"), 0)
  verify(List("add", "tpoint1", "tdir_b"), 0)

  verify(List("add", "tpointX", "nonexistent"), 1)

  val listResult = verify(List("list"), 0)

  assert(listResult.find(l => l.contains("tpoint0") && l.contains("tdir_a")).isDefined, "tpoint0 expected on list")
  assert(listResult.find(l => l.contains("tpoint1") && l.contains("tdir_b")).isDefined, "tpoint1 expected on list")
  assert(listResult.find(_.contains("tpointX")).isEmpty, "tpointX not expected on list")
  assert(listResult.find(_.contains("tpointX")).isEmpty, "nonexistent not expected on list")

  println("here:")
  println(listResult.size)

  assert(listResult.size == 3, s"size == ${listResult.size}, expected: 3")

  val colorlessListResult = verify(List("--no-colors", "list"), 0)

  assert(colorlessListResult.size == 2, s"size == ${colorlessListResult.size}, expected: 2")

  println(fansi.Color.Green("Smoke test succeeded"))
} catch {
  case e: Throwable =>
    println(fansi.Color.Red("Smoke test failed") + s": $e, ${e.getMessage}")
    e.printStackTrace()
    os.remove.all(testDir)
    sys.exit(1)
} finally {
  os.remove.all(testDir)
  sys.exit(0)
}

def verify(arguments: List[String], expectedExitCode: Int): Vector[String] = {
  val cmd = "../teleport-scala"
  val res = os.proc(cmd :: arguments).call(cwd = os.pwd / "teleport_guinea_pig", check = false)
  assert(res.exitCode == expectedExitCode, s"Unexpected status code: ${res.exitCode}, expected: ${expectedExitCode} for $cmd")
  res.out.lines
}

def fail(msg: String) = {
  println(msg)
  sys.exit(1)
}
