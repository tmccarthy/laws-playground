val settingsHelper = ProjectSettingsHelper("au.id.tmm", "laws-playground")(
  githubProjectName = "laws-playground",
)

settingsHelper.settingsForBuild

lazy val root = project
  .in(file("."))
  .settings(settingsHelper.settingsForRootProject)
  .settings(console := (console in Compile in core).value)
  .aggregate(
    core,
  )

val catsVersion        = "2.2.0-RC2"

lazy val core = project
  .in(file("core"))
  .settings(settingsHelper.settingsForSubprojectCalled("core"))
  .settings(
    libraryDependencies += "org.typelevel" %% "cats-core"        % catsVersion,
    libraryDependencies += "org.typelevel" %% "cats-kernel-laws" % catsVersion,
    libraryDependencies += "org.scala-lang" % "scala-reflect"    % scalaVersion.value,
  )

lazy val examples = project
  .in(file("examples"))
  .settings(settingsHelper.settingsForSubprojectCalled("examples"))
  .settings(publish / skip := true)
  .dependsOn(core)

addCommandAlias("check", ";+test;scalafmtCheckAll")
