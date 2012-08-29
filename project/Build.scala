import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "molxchange"
    val appVersion      = "1.0"

    val appDependencies = Seq(
      "uk.ac.cam.ch.opsin" % "opsin-core" % "1.2.0",
      "uk.ac.cam.ch.opsin" % "opsin-inchi" % "1.2.0",
      "commons-codec" % "commons-codec" % "1.5"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      resolvers += "OPSIN repository at Cambridge" at "https://maven.ch.cam.ac.uk/content/repositories/releases/uk/ac/cam/ch/opsin/"
    )

}
