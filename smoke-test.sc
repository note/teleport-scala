import $ivy.`com.lihaoyi::os-lib:0.6.3`
import $ivy.`com.lihaoyi::os-lib:0.2.9`

val testDir = os.pwd / "teleport_guinea_pig"

if(os.exists(testDir)) {
  println(s"$testDir already exists")
  sys.exit(2)
}

try {
  os.makeDir(testDir)
  List("a", "b", "c", "d").map(testDir / _).foreach(os.makeDir)

  verify(List("list"), 0)

  verify(List("add", "tpoint0", "a"), 0)

  verify(List("add", "tpointX", "nonexistent"), 1)

  val r = verify(List("list"), 0)

  assert(r.find(_.contains("tpoint0")).isDefined, "tpoint0 expected on list")
  assert(r.find(_.contains("tpointX")).isEmpty, "tpointX not expected on list")

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
