name := "turkce-metin-ozetleme-scala"

version := "0.1"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.8"

dockerBaseImage := "nimmis/java:oracle-8-jdk"

fork in run := true

javaOptions ++= Seq(
  "-Dlog.service.output=/dev/stderr",
  "-Dlog.access.output=/dev/stderr")

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  "Twitter Maven" at "https://maven.twttr.com",
  "sxfcode Bintray Repo" at "https://dl.bintray.com/sfxcode/maven/",
  "ahmetaa-repo" at "https://raw.github.com/ahmetaa/maven-repo/master"
)

lazy val versions = new {
  val finatra = "2.1.4"
  val guice = "4.0"
  val logback = "1.0.13"
  val mockito = "1.9.5"
  val scalatest = "2.2.3"
  val specs2 = "2.3.12"
  val swagger = "0.5.0"
  val mongo = "2.1.0"
  val config = "1.3.2"
  val zemberekCore = "0.13.0"
  val zemberekTokenization = "0.13.0"
  val zemberekMorphology = "0.13.0"
  val zemberekNormalization = "0.13.0"

}

libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-core" % "3.5.4",
  "org.mongodb.scala" %% "mongo-scala-driver" % versions.mongo,
  "com.github.xiaodongw" %% "swagger-finatra2" % versions.swagger,

  "com.twitter.finatra" %% "finatra-http" % versions.finatra,
  "com.twitter.finatra" %% "finatra-slf4j" % versions.finatra,
  "com.twitter.finatra" %% "finatra-httpclient" % versions.finatra,
  "ch.qos.logback" % "logback-classic" % versions.logback,
  "com.typesafe" % "config" % versions.config,

  "zemberek-nlp" % "zemberek-core" % versions.zemberekCore,
  "zemberek-nlp" % "zemberek-tokenization" % versions.zemberekTokenization,
  "zemberek-nlp" % "zemberek-morphology" % versions.zemberekMorphology,
  "zemberek-nlp" % "zemberek-normalization" % versions.zemberekTokenization,


"ch.qos.logback" % "logback-classic" % versions.logback % "test",
  "com.twitter.finatra" %% "finatra-http" % versions.finatra % "test",
  "com.twitter.finatra" %% "finatra-jackson" % versions.finatra % "test",
  "com.twitter.inject" %% "inject-server" % versions.finatra % "test",
  "com.twitter.inject" %% "inject-app" % versions.finatra % "test",
  "com.twitter.inject" %% "inject-core" % versions.finatra % "test",
  "com.twitter.inject" %% "inject-modules" % versions.finatra % "test",
  "com.google.inject.extensions" % "guice-testlib" % versions.guice % "test",

  "com.twitter.finatra" %% "finatra-http" % versions.finatra % "test" classifier "tests",
  "com.twitter.finatra" %% "finatra-jackson" % versions.finatra % "test" classifier "tests",
  "com.twitter.inject" %% "inject-server" % versions.finatra % "test" classifier "tests",
  "com.twitter.inject" %% "inject-app" % versions.finatra % "test" classifier "tests",
  "com.twitter.inject" %% "inject-core" % versions.finatra % "test" classifier "tests",
  "com.twitter.inject" %% "inject-modules" % versions.finatra % "test" classifier "tests",

  "org.mockito" % "mockito-core" % versions.mockito % "test",
  "org.scalatest" %% "scalatest" % versions.scalatest % "test",
  "org.specs2" %% "specs2" % versions.specs2 % "test")

enablePlugins(JavaAppPackaging)