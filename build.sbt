import Dependencies._
import com.typesafe.sbt.SbtNativePackager.autoImport.maintainer
import sbt.Compile

lazy val root = (project in file("."))
  .settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.13.4",
      version := "0.1.0"
    )),
    publishArtifact := false,
    name := "scala-exercise",
  ).enablePlugins(DockerPlugin, JavaAppPackaging, AkkaGrpcPlugin)

lazy val proxy = (project in file("proxy"))
  .settings(
    name := "proxy",
    libraryDependencies ++= ProxyDependencies,
    version := "0.1.0",
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
    version := "0.1.0",
    maintainer in Docker := "Pau Alarcón",
  )
  .enablePlugins(DockerPlugin, JavaAppPackaging)
  .dependsOn(common)

enablePlugins(AkkaGrpcPlugin)

lazy val common = (project in file("common"))
  .settings(
    name := "common",
    version := "0.1.0",
    akkaGrpcGeneratedLanguages := Seq(AkkaGrpc.Scala),
    Compile / PB.protoSources ++= (Compile / PB.protoSources).value

).enablePlugins(AkkaGrpcPlugin)

lazy val integrationTests = (project in file("integration-tests"))
  .settings(
    name := "integrationTests",
    libraryDependencies ++= PrimeNumbersProviderDependencies,
    version := "0.1.0",
  )
  .enablePlugins(DockerPlugin, JavaAppPackaging)
  .dependsOn(common)