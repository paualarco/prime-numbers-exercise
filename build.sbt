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
    name := "dispatcher",
    libraryDependencies ++= ProxyDependencies,
    version := "0.1.0",
    maintainer in Docker := "Pau Alarcón",
    dockerBaseImage in Docker := "golang:1.10-alpine3.7"
  )
  .enablePlugins(DockerPlugin, JavaAppPackaging)
  .dependsOn(common)
  .aggregate(common)

lazy val primeNumbersProvider = (project in file("prime-numbers-server"))
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
    libraryDependencies ++= CommonDependencies,
   // Compile / PB.protoSources := Seq(new File("src/main/protobuf")),
    version := "0.0.1",
    //PB.protoSources.in(Compile) := Seq(sourceDirectory.value / "src" / "main" / "proto") ,
    akkaGrpcGeneratedLanguages := Seq(AkkaGrpc.Scala),
    Compile / PB.protoSources ++= (Compile / PB.protoSources).value

).enablePlugins(AkkaGrpcPlugin)
