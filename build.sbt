import com.peknight.build.gav.*
import com.peknight.build.sbt.*

commonSettings

lazy val api = (project in file("."))
  .settings(name := "api")
  .aggregate(apiCore.projectRefs *)
  .aggregate(apiInstances)

lazy val apiCore = (projectMatrix in file("api-core"))
  .settings(name := "api-core")
  .settings(libraryDependencies ++= dependencies(peknight.error))
  .jvmPlatform(scalaVersions = Seq(scala.scala3.version))
  .jsPlatform(scalaVersions = Seq(scala.scala3.version))
  .nativePlatform(scalaVersions = Seq(scala.scala3.version))

lazy val apiInstances = (project in file("api-instances"))
  .settings(name := "api-instances")
  .aggregate(apiCodecInstances.projectRefs *)

lazy val apiCodecInstances = (projectMatrix in file("api-instances/codec"))
  .dependsOn(apiCore)
  .settings(name := "api-codec-instances")
  .settings(libraryDependencies ++= dependencies(peknight.codec))
  .jvmPlatform(scalaVersions = Seq(scala.scala3.version))
  .jsPlatform(scalaVersions = Seq(scala.scala3.version))
