name := "probability"

ThisBuild / tlBaseVersion := "0.1"

xerial.sbt.Sonatype.SonatypeKeys.sonatypeProfileName := "au.id.tmm"
Global / xerial.sbt.Sonatype.SonatypeKeys.sonatypeProfileName := "au.id.tmm"
ThisBuild / xerial.sbt.Sonatype.SonatypeKeys.sonatypeProfileName := "au.id.tmm"
ThisBuild / organization := "au.id.tmm.laws-playground"
ThisBuild / organizationName := "Timothy McCarthy"
ThisBuild / startYear := Some(2019)
ThisBuild / developers := List(
  tlGitHubDev("tmccarthy", "Timothy McCarthy"),
)

val Scala213 = "2.13.13"
val Scala3   = "3.2.1"
ThisBuild / scalaVersion := Scala213
ThisBuild / crossScalaVersions := Seq(
//  Scala3,
//  Scala213,
)

ThisBuild / githubWorkflowJavaVersions := List(
  JavaSpec.temurin("17"),
)

ThisBuild / tlCiHeaderCheck := false
ThisBuild / tlCiScalafmtCheck := true
ThisBuild / tlCiMimaBinaryIssueCheck := false
ThisBuild / tlCiDependencyGraphJob := false
ThisBuild / tlFatalWarnings := true
ThisBuild / tlSonatypeUseLegacyHost := true

addCommandAlias("check", ";githubWorkflowCheck;scalafmtSbtCheck;+scalafmtCheckAll;+test")
addCommandAlias("fix", ";githubWorkflowGenerate;scalafmtSbt;+scalafmtAll")

lazy val root = tlCrossRootProject
  .settings(console := (core / Compile / console).value)
  .aggregate(
    core,
    examples,
  )

val catsVersion           = "2.10.0"
val tmmCollectionsVersion = "0.2.0"
val tmmUtilsVersion       = "0.10.0"
val circeVersion          = "0.14.3"
val slf4jVersion          = "1.7.30"

lazy val core = project
  .in(file("core"))
  .settings(name := "laws-playground-core")
  .enablePlugins(NoPublishPlugin)
  .settings(
    libraryDependencies += "au.id.tmm.tmm-utils"             %% "tmm-utils-syntax"                 % tmmUtilsVersion,
    libraryDependencies += "au.id.tmm.tmm-utils"             %% "tmm-utils-errors"                 % tmmUtilsVersion,
    libraryDependencies += "org.apache.commons"               % "commons-text"                     % "1.11.0",
    libraryDependencies += "commons-io"                       % "commons-io"                       % "2.7",
    libraryDependencies += "org.typelevel"                   %% "algebra-laws"                     % catsVersion,
    libraryDependencies += "org.typelevel"                   %% "cats-kernel-laws"                 % catsVersion,
    libraryDependencies += "io.circe"                        %% "circe-core"                       % circeVersion,
    libraryDependencies += "org.slf4j"                        % "slf4j-api"                        % slf4jVersion,
    libraryDependencies += "org.scala-lang.modules"          %% "scala-parallel-collections"       % "1.0.4",
    libraryDependencies += "au.id.tmm.tmm-scala-collections" %% "tmm-scala-collections-scalacheck" % tmmCollectionsVersion % Test,
  )

lazy val examples = project
  .in(file("examples"))
  .settings(name := "laws-playground-examples")
  .enablePlugins(NoPublishPlugin)
  .settings(
    libraryDependencies += "org.typelevel"                   %% "cats-core"                  % catsVersion,
    libraryDependencies += "org.typelevel"                   %% "cats-laws"                  % catsVersion,
    libraryDependencies += "au.id.tmm.tmm-scala-collections" %% "tmm-scala-collections-core" % tmmCollectionsVersion,
    libraryDependencies += "au.id.tmm.tmm-scala-collections" %% "tmm-scala-collections-cats" % tmmCollectionsVersion,
    libraryDependencies += "au.id.tmm.tmm-utils"             %% "tmm-utils-cats"             % tmmUtilsVersion,
    libraryDependencies += "org.slf4j"                        % "slf4j-simple"               % slf4jVersion,
    libraryDependencies += "org.typelevel"                   %% "spire-laws"                 % "0.18.0",
  )
  .dependsOn(core)
