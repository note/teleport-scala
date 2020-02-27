import Common._
import Dependencies._
import wartremover.Wart

lazy val root = (project in file("."))
  .enablePlugins(BuildInfoPlugin, GitVersioning)
  .commonSettings("teleport-scala")
  .settings(
    libraryDependencies ++= mainDeps ++ testDeps,
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion, git.baseVersion, git.gitHeadCommit),
    buildInfoPackage := "pl.msitko.teleport",
    buildInfoUsePackageAsPath := true,
  )
  .settings(
    wartremoverWarnings in (Compile, compile) --= Seq(
      Wart.StringPlusAny
    )
  )
