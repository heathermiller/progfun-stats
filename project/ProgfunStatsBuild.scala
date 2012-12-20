import sbt._
import sbt.Keys._

object ProgfunStatsBuild extends Build {

  lazy val progfunStats = Project(
    id = "progfun-stats",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "Progfun Stats",
      organization := "progfun",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.9.2"
      // add other settings here
    )
  )
}
