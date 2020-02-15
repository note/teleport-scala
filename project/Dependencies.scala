import sbt._

object Dependencies {
  lazy val mainDeps = Seq (
    "org.typelevel" %% "cats-effect"  % "2.1.1",
    "com.monovore"  %% "decline"      % "1.0.0",
    "com.lihaoyi"   %% "fansi"        % "0.2.9",
    "com.lihaoyi"   %% "os-lib"       % "0.6.3"
  )

  lazy val testDeps = Seq("org.scalatest" %% "scalatest" % "3.1.0" % Test)
}
