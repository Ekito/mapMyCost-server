import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "mapMyCost-server"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      // Add your project dependencies here,
      
      "commons-io" % "commons-io" % "2.0.1", 
      "com.google.code.gson" % "gson" % "1.1"
       
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
      // Add your own project settings here      

      // Twitter Bootstrap v2.0.1 compilation (https://plus.google.com/u/0/108788785914419775677/posts/QgyUF9cXPkv)
      lessEntryPoints <<= (sourceDirectory in Compile)(base => (
                (base / "assets" / "stylesheets" / "twitterbootstrap" / "bootstrap.less") +++
                (base / "assets" / "stylesheets" / "twitterbootstrap" / "responsive.less")
                ))

    )

}
