ThisBuild / name := "compiler-plugin-demo9"

lazy val commonSettings = Seq(
  scalaVersion := "2.13.3",
  version := "1.0",
)

lazy val plugin = project
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      scalaOrganization.value % "scala-compiler" % scalaVersion.value,
    )
  )
