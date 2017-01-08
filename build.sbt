name := """reminder-app-play"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

javaOptions in Test += "-Dconfig.file=conf/application.test.conf"

libraryDependencies ++= Seq(
  jdbc,
  evolutions,
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  "org.postgresql" % "postgresql" % "9.4.1212"
)

