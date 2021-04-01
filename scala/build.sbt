organization := "fr.edgewhere"
name := "redacted-jar"
version := "0.0.1"
scalaVersion := "2.13.1"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", _) => MergeStrategy.discard
  case _ => MergeStrategy.first
}
mainClass in assembly := Some("fr.edgewhere.redacted.Main")
assemblyJarName in assembly := s"${name.value}-${version.value}.jar"

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

libraryDependencies ++= Seq(
  "com.github.scopt" %% "scopt" % "4.0.0-RC2",
  "org.scalatest" %% "scalatest" % "3.0.8"
)
