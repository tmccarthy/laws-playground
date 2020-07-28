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

val catsVersion           = "2.2.0-RC2"
val tmmCollectionsVersion = "0.0.3"
val tmmUtilsVersion       = "0.6.2"
val circeVersion          = "0.14.0-M1"

lazy val core = project
  .in(file("core"))
  .settings(settingsHelper.settingsForSubprojectCalled("core"))
  .settings(
    libraryDependencies += "au.id.tmm.tmm-utils"             %% "tmm-utils-cats"                   % tmmUtilsVersion,
    libraryDependencies += "au.id.tmm.tmm-utils"             %% "tmm-utils-syntax"                 % tmmUtilsVersion,
    libraryDependencies += "au.id.tmm.tmm-scala-collections" %% "tmm-scala-collections-core"       % tmmCollectionsVersion,
    libraryDependencies += "au.id.tmm.tmm-scala-collections" %% "tmm-scala-collections-cats"       % tmmCollectionsVersion,
    libraryDependencies += "org.typelevel"                   %% "cats-core"                        % catsVersion,
    libraryDependencies += "org.typelevel"                   %% "cats-kernel-laws"                 % catsVersion,
    libraryDependencies += "org.apache.commons"               % "commons-text"                     % "1.9",
    libraryDependencies += "commons-io"                       % "commons-io"                       % "2.7",
    libraryDependencies += "io.circe"                        %% "circe-core"                       % circeVersion,
    libraryDependencies += "au.id.tmm.tmm-scala-collections" %% "tmm-scala-collections-scalacheck" % tmmCollectionsVersion % Test,
  )

lazy val examples = project
  .in(file("examples"))
  .settings(settingsHelper.settingsForSubprojectCalled("examples"))
  .settings(publish / skip := true)
  .dependsOn(core)

addCommandAlias("check", ";+test;scalafmtCheckAll")
