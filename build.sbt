ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.6.2"

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
    ),
  )
