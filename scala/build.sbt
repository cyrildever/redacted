organization := "fr.edgewhere"
name := "redacted-jar"
version := "0.3.2"
scalaVersion := "2.12.13"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", _) => MergeStrategy.discard
  case _ => MergeStrategy.first
}
mainClass in assembly := Some("fr.edgewhere.redacted.Main")
assemblyJarName in assembly := s"${name.value}-${version.value}.jar"
javacOptions ++= Seq("-source", "17", "-target", "1.8")

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

resolvers += "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"

libraryDependencies ++= Seq(
  "com.github.scopt" %% "scopt" % "4.0.1",
  "org.scalatest" %% "scalatest" % "3.0.8", // Do not update to latest yet
  "org.scorexfoundation" %% "scrypto" % "2.1.10",
  "fr.edgewhere" %% "feistel" % "1.4.0"
)
