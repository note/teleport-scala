import sbt._

object Dependencies {
  val circeVersion = "0.13.0"

  lazy val mainDeps = Seq (
    "org.typelevel" %% "cats-effect"    % "2.1.1",
    "com.monovore"  %% "decline"        % "1.0.0",
    "com.lihaoyi"   %% "fansi"          % "0.2.9",
    "com.lihaoyi"   %% "os-lib"         % "0.6.3",
    "io.circe"      %% "circe-core"     % circeVersion,
    "io.circe"      %% "circe-parser"   % circeVersion,
    "io.circe"      %% "circe-generic"  % circeVersion
  )

  lazy val testDeps = Seq("org.scalatest" %% "scalatest" % "3.1.0" % Test)
}
