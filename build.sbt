ThisBuild / scalaVersion     := "2.13.16"

ThisBuild / libraryDependencies ++= List(
  "com.softwaremill.sttp.client3" %% "core" % "3.11.0",
  "com.softwaremill.sttp.client3" %% "circe" % "3.11.0",
  "io.circe" %% "circe-core" % "0.14.14",
  "io.circe" %% "circe-generic" % "0.14.14",
  "io.circe" %% "circe-parser" % "0.14.14",
  "org.scala-lang.modules" %% "scala-parallel-collections" % "1.2.0",
  "org.eclipse.angus" % "angus-mail" % "2.0.4",
  "com.azure" % "azure-identity" % "1.17.0",
  "com.microsoft.graph" % "microsoft-graph" % "6.51.0",
  "com.google.api-client" % "google-api-client" % "2.8.1",
  "com.google.oauth-client" % "google-oauth-client-jetty" % "1.39.0",
  "com.google.apis" % "google-api-services-gmail" % "v1-rev20250630-2.0.0",
  "com.google.apis" % "google-api-services-drive" % "v3-rev20250829-2.0.0",
  "com.dropbox.core" % "dropbox-core-sdk" % "7.0.0",
  "com.amilesend" % "onedrive-java-sdk" % "2.1",
)

lazy val root = (project in file("."))
  .settings(
    name := "Liftoff",
  )
