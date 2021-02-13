import sbt._

object Dependencies {

  object DependencyVersions {
    val PureConfig = "0.14.0"
    val Monix = "3.3.0"
    val MonixConnect = "0.5.2"
    val Circe = "0.12.3"
    val Akka = "2.6.9"
    val AkkaHttp = "10.2.3"

    val Log4jScala = "11.0"
    val Log4j = "2.10.0"
    val ScalaLogging = "3.9.2"

    val Scalatest = "3.2.3"
    val Scalacheck = "1.13.5"
  }

  private val TestDependencies = Seq(
    "org.scalatest" %% "scalatest" % DependencyVersions.Scalatest
   // "org.scalacheck" %% "scalacheck" % DependencyVersions.Scalacheck
  ).map(_ % Test)

  val ProxyDependencies: Seq[ModuleID] = Seq(
    "com.typesafe.akka" %% "akka-http-spray-json" % DependencyVersions.AkkaHttp,
    "com.typesafe.akka" %% "akka-http" % DependencyVersions.AkkaHttp,
    "com.typesafe.akka" %% "akka-stream" % DependencyVersions.Akka,
    "com.typesafe.scala-logging" %% "scala-logging" % DependencyVersions.ScalaLogging,
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "com.github.pureconfig" %% "pureconfig" % DependencyVersions.PureConfig,
    "com.typesafe.akka" %% "akka-stream-testkit" % DependencyVersions.Akka % Test,
    "com.typesafe.akka" %% "akka-http-testkit" % DependencyVersions.AkkaHttp % Test
  ) ++ TestDependencies

  val PrimeNumbersProviderDependencies: Seq[ModuleID] = Seq(
    "io.monix" %% "monix" % "3.3.0",
    "com.typesafe.akka" %% "akka-stream" % DependencyVersions.Akka,
    "com.typesafe.scala-logging" %% "scala-logging" % DependencyVersions.ScalaLogging,
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "com.github.pureconfig" %% "pureconfig" % DependencyVersions.PureConfig
  ) ++ TestDependencies

  val CommonDependencies: Seq[ModuleID] = Seq(
  )

}