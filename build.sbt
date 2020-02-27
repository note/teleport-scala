import Common._
import Dependencies._

lazy val root = (project in file("."))
  .enablePlugins(BuildInfoPlugin, GitVersioning)
  .commonSettings("teleport-scala")
  .settings(
    libraryDependencies ++= mainDeps ++ testDeps,
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion, git.baseVersion, git.gitHeadCommit),
    buildInfoPackage := "pl.msitko.teleport",
    buildInfoUsePackageAsPath := true,
  )
