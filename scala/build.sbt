organization := "com.cyrildever"
name := "redacted-jar"
version := "1.0.8"
scalaVersion := "2.12.13"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", _) => MergeStrategy.discard
  case _ => MergeStrategy.first
}
mainClass in assembly := Some("com.cyrildever.redacted.Main")
assemblyJarName in assembly := s"${name.value}-${version.value}.jar"

resolvers += "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"
resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies ++= Seq(
  "com.github.scopt" %% "scopt" % "4.1.0",
  "org.scalatest" %% "scalatest" % "3.2.19" % "test",
  "org.scorexfoundation" %% "scrypto" % "3.0.0",
  "com.cyrildever" %% "feistel-jar" % "1.5.7"
)
