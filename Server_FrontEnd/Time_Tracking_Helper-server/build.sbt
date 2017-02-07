name := """Time_Tracking_Helper-server"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)
scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  filters,
   "org.mindrot" % "jbcrypt" % "0.3m"
)


fork in run := false