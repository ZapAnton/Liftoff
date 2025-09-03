ThisBuild / scalaVersion     := "2.13.16"

ThisBuild / libraryDependencies ++= List(
  "org.scala-lang.modules" %% "scala-parallel-collections" % "1.2.0",
  "org.eclipse.angus" % "angus-mail" % "2.0.3",
  "com.azure" % "azure-identity" % "1.17.0",
  "com.microsoft.graph" % "microsoft-graph" % "6.49.0",
  "com.google.api-client" % "google-api-client" % "2.0.0",
  "com.google.oauth-client" % "google-oauth-client-jetty" % "1.34.1",
  "com.google.apis" % "google-api-services-drive" % "v3-rev197-1.25.0",
  "com.google.apis" % "google-api-services-gmail" % "v1-rev20220404-2.0.0",
  "com.dropbox.core" % "dropbox-core-sdk" % "7.0.0",
  "com.amilesend" % "onedrive-java-sdk" % "2.0.1",
)

lazy val root = (project in file("."))
  .settings(
    name := "Liftoff",
  )
