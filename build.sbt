import Dependencies._
import com.typesafe.sbt.SbtNativePackager.autoImport.maintainer
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport.dockerBaseImage
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
  )
  .dependsOn(proxy, primeNumbersServer, common)
  .aggregate(proxy, primeNumbersServer, common)

lazy val proxy = (project in file("proxy"))
  .settings(
    name := "proxy",
    libraryDependencies ++= ProxyDependencies,
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
    version := Version.primeNumbersServer,
    maintainer in Docker := "Pau Alarcón",
    dockerBaseImage in Docker := "golang:1.10-alpine3.7"
  )
  .enablePlugins(DockerPlugin, JavaAppPackaging)
  .dependsOn(common)

lazy val common = (project in file("common"))
  .settings(
    name := "common",
    version := Version.common,
    akkaGrpcGeneratedLanguages := Seq(AkkaGrpc.Scala),
    Compile / PB.protoSources ++= (Compile / PB.protoSources).value ,
).enablePlugins(AkkaGrpcPlugin)

lazy val integrationTests = (project in file("integration-tests"))
  .settings(
    name := "integrationTests",
    libraryDependencies ++= IntegrationTests,
    version := "0.1.0",
  )