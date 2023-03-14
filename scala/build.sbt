organization := "com.cyrildever"
name := "redacted-jar"
version := "0.6.0"
scalaVersion := "2.12.13"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", _) => MergeStrategy.discard
  case _ => MergeStrategy.first
}
mainClass in assembly := Some("com.cyrildever.redacted.Main")
assemblyJarName in assembly := s"${name.value}-${version.value}.jar"

resolvers += "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"

libraryDependencies ++= Seq(
  "com.github.scopt" %% "scopt" % "4.1.0",
  "org.scalatest" %% "scalatest" % "3.0.8", // Do not update to latest yet
  "org.scorexfoundation" %% "scrypto" % "2.2.1",
  "com.cyrildever" %% "feistel-jar" % "1.5.0"
)
