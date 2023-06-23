import sbt.*

object Dependencies {
  object Versions {
    lazy val http4s = "0.23.21"
    lazy val logback = "1.4.8"
  }

  trait LibGroup {
    def all: Seq[ModuleID]
  }

  object org {
    object http4s extends LibGroup {
      def http4s(artifact: String): ModuleID =
        "org.http4s" %% s"http4s-$artifact" % Versions.http4s

      lazy val core = http4s("core")
      lazy val server = http4s("ember-server")
      lazy val dsl = http4s("dsl")

      override def all: Seq[sbt.ModuleID] = Seq(core, server, dsl)
    }
  }
  object ch {
    object qos {
      lazy val logback = "ch.qos.logback" % "logback-classic" % Versions.logback
    }
  }
}
