import Dependencies._
import com.typesafe.sbt.SbtNativePackager.autoImport.maintainer
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport.dockerBaseImage
import sbt.Compile

lazy val sharedSettings = Seq(
  scalaVersion       := "2.13.4",
  scalafmtOnCompile  := true,
  organization := "com.dixa.exercise",

  scalacOptions in (Compile, console) ++= Seq("-Ywarn-unused-import"),
  // Linter
  scalacOptions ++= Seq(
    "-Ywarn-unused:imports", // Warn if an import selector is not referenced.
    "-Ywarn-dead-code", // Warn when dead code is identified.
    // Turns all warnings into errors ;-)
    //temporary disabled for mongodb warn, -YWarn (2.13) and Silencer (2.12) should fix it...
    //"-Xfatal-warnings", //Turning of fatal warnings for the moment
    // Enables linter options
    "-Xlint:adapted-args", // warn if an argument list is modified to match the receiver
    "-Xlint:infer-any", // warn when a type argument is inferred to be `Any`
    "-Xlint:missing-interpolator", // a string literal appears to be missing an interpolator id
    "-Xlint:doc-detached", // a ScalaDoc comment appears to be detached from its element
    "-Xlint:private-shadow", // a private field (or class parameter) shadows a superclass field
    "-Xlint:type-parameter-shadow", // a local type parameter shadows a type already in scope
    "-Xlint:poly-implicit-overload", // parameterized overloaded implicit methods are not visible as view bounds
    "-Xlint:option-implicit", // Option.apply used implicit view
    "-Xlint:delayedinit-select", // Selecting member of DelayedInit
    //"-Xlint:package-object-classes" // Class or object defined in package object
  ),
  parallelExecution in Test             := true
)

lazy val root = (project in file("."))
  .settings(
    publishArtifact := false,
    name := "scala-exercise",
    sharedSettings: _*
  )
  .dependsOn(proxy, primeNumbersServer, common)
  .aggregate(proxy, primeNumbersServer, common)

lazy val proxy = (project in file("proxy"))
  .settings(
    name := "proxy",
    libraryDependencies ++= ProxyDependencies,
    sharedSettings: _*,
    version := Version.proxy,
    maintainer in Docker := "Pau Alarcón",
    dockerBaseImage in Docker := "golang:1.10-alpine3.7"
  )
  .enablePlugins(DockerPlugin, JavaAppPackaging)
  .dependsOn(common)
  .aggregate(common)

lazy val primeNumbersServer = (project in file("prime-numbers-server"))
  .settings(
    name := "primeNumbersServer",
    libraryDependencies ++= PrimeNumbersProviderDependencies,
    sharedSettings: _*,
    version := Version.primeNumbersServer,
    maintainer in Docker := "Pau Alarcón",
    dockerBaseImage in Docker := "golang:1.10-alpine3.7"
  )
  .enablePlugins(DockerPlugin, JavaAppPackaging)
  .dependsOn(common)

lazy val common = (project in file("common"))
  .settings(
    name := "common",
    sharedSettings: _*,
    version := Version.common,
    akkaGrpcGeneratedLanguages := Seq(AkkaGrpc.Scala),
    Compile / PB.protoSources ++= (Compile / PB.protoSources).value ,
).enablePlugins(AkkaGrpcPlugin)

val IT = config("it") extend Test

lazy val integrationTests = (project in file("integration-tests"))
  .configs(IntegrationTest, IT)
  .settings(
    name := "integrationTests",
    libraryDependencies ++= IntegrationTests,
    version := "0.1.0",
  )