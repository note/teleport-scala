import Common._
import Dependencies._

lazy val root = (project in file("."))
  .commonSettings("teleport-scala", "0.1.0")
  .settings(
    libraryDependencies ++= mainDeps ++ testDeps
  )
