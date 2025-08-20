import com.peknight.build.gav.*
import com.peknight.build.sbt.*

commonSettings

lazy val api = (project in file("."))
  .settings(name := "api")
  .aggregate(
    apiCore.jvm,
    apiCore.js,
    apiCore.native,
    apiInstances,
  )

lazy val apiCore = (crossProject(JVMPlatform, JSPlatform, NativePlatform) in file("api-core"))
  .settings(name := "api-core")
  .settings(crossDependencies(peknight.error))

lazy val apiInstances = (project in file("api-instances"))
  .settings(name := "api-instances")
  .aggregate(
    apiCodecInstances.jvm,
    apiCodecInstances.js,
  )

lazy val apiCodecInstances = (crossProject(JVMPlatform, JSPlatform) in file("api-instances/codec"))
  .dependsOn(apiCore)
  .settings(name := "api-codec-instances")
  .settings(crossDependencies(peknight.codec))
