import com.softwaremill.SbtSoftwareMill.autoImport.{commonSmlBuildSettings, ossPublishSettings, wartRemoverSettings}
import org.scalafmt.sbt.ScalafmtPlugin.autoImport.scalafmtOnCompile
import sbt.Keys.{name, organization, scalaVersion, version}
import sbt.Project

object Common {
  implicit class ProjectFrom(project: Project) {
    def commonSettings(nameArg: String, versionArg: String): Project = project.settings(
      name := nameArg,
      organization := "pl.msitko",
      version := versionArg,

      scalaVersion := "2.13.1",
      scalafmtOnCompile := true,

      commonSmlBuildSettings,
      wartRemoverSettings
    )
  }
}