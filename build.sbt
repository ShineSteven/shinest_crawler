name := """crawler"""

lazy val root = (project in file("."))
  .settings(Settings.commonSettings: _*)
  .settings(Settings.assemblySettings: _*)
  .settings(
    libraryDependencies ++= Dependencies.crawler
  ).settings(
  mainClass in assembly := Some("shine.st.crawler.BoxOfficeCrawler"),
  mainClass in (Compile, run) := Some("shine.st.crawler.BoxOfficeCrawler"),
)
