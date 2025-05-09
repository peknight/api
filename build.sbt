ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.7.0"

ThisBuild / organization := "com.peknight"

lazy val commonSettings = Seq(
  scalacOptions ++= Seq(
    "-feature",
    "-deprecation",
    "-unchecked",
    "-Xfatal-warnings",
    "-language:strictEquality",
    "-Xmax-inlines:64"
  ),
)

lazy val api = (project in file("."))
  .aggregate(
    apiCore.jvm,
    apiCore.js,
    apiInstances,
  )
  .settings(commonSettings)
  .settings(
    name := "api",
  )

lazy val apiCore = (crossProject(JSPlatform, JVMPlatform) in file("api-core"))
  .settings(commonSettings)
  .settings(
    name := "api-core",
    libraryDependencies ++= Seq(
      "com.peknight" %%% "error-core" % pekErrorVersion,
    ),
  )

lazy val apiInstances = (project in file("api-instances"))
  .aggregate(
    apiCodecInstances.jvm,
    apiCodecInstances.js,
  )
  .settings(commonSettings)
  .settings(
    name := "api-instances",
    libraryDependencies ++= Seq(
    ),
  )

lazy val apiCodecInstances = (crossProject(JSPlatform, JVMPlatform) in file("api-instances/codec"))
  .dependsOn(apiCore)
  .settings(commonSettings)
  .settings(
    name := "api-codec-instances",
    libraryDependencies ++= Seq(
      "com.peknight" %%% "codec-core" % pekCodecVersion,
    ),
  )

val pekVersion = "0.1.0-SNAPSHOT"
val pekErrorVersion = pekVersion
val pekCodecVersion = pekVersion
