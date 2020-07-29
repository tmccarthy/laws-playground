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

val catsVersion             = "2.2.0-RC2"
val typelevelAlgebraVersion = "2.0.0"
val tmmCollectionsVersion   = "0.0.3"
val tmmUtilsVersion         = "0.6.2"
val circeVersion            = "0.14.0-M1"
val slf4jVersion            = "1.7.30"

lazy val core = project
  .in(file("core"))
  .settings(settingsHelper.settingsForSubprojectCalled("core"))
  .settings(
    libraryDependencies += "au.id.tmm.tmm-utils"             %% "tmm-utils-syntax"                 % tmmUtilsVersion,
    libraryDependencies += "au.id.tmm.tmm-utils"             %% "tmm-utils-errors"                 % tmmUtilsVersion,
    libraryDependencies += "org.apache.commons"               % "commons-text"                     % "1.9",
    libraryDependencies += "commons-io"                       % "commons-io"                       % "2.7",
    libraryDependencies += "org.typelevel"                   %% "algebra-laws"                     % typelevelAlgebraVersion,
    libraryDependencies += "org.typelevel"                   %% "cats-kernel-laws"                 % catsVersion,
    libraryDependencies += "io.circe"                        %% "circe-core"                       % circeVersion,
    libraryDependencies += "org.slf4j"                        % "slf4j-api"                        % slf4jVersion,
    libraryDependencies += "org.scala-lang.modules"          %% "scala-parallel-collections"       % "0.2.0",
    libraryDependencies += "au.id.tmm.tmm-scala-collections" %% "tmm-scala-collections-scalacheck" % tmmCollectionsVersion % Test,
  )

lazy val examples = project
  .in(file("examples"))
  .settings(settingsHelper.settingsForSubprojectCalled("examples"))
  .settings(publish / skip := true)
  .settings(
    libraryDependencies += "org.typelevel"                   %% "cats-core"                  % catsVersion,
    libraryDependencies += "org.typelevel"                   %% "cats-laws"                  % catsVersion,
    libraryDependencies += "au.id.tmm.tmm-scala-collections" %% "tmm-scala-collections-core" % tmmCollectionsVersion,
    libraryDependencies += "au.id.tmm.tmm-scala-collections" %% "tmm-scala-collections-cats" % tmmCollectionsVersion,
    libraryDependencies += "au.id.tmm.tmm-utils"             %% "tmm-utils-cats"             % tmmUtilsVersion,
    libraryDependencies += "org.slf4j"                        % "slf4j-simple"               % slf4jVersion,
  )
  .dependsOn(core)

addCommandAlias("check", ";+test;scalafmtCheckAll")
